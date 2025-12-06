package com.cczu.nosql.message;

import com.cczu.nosql.constant.RedisKeyConstant;
import com.cczu.nosql.entity.Article;
import com.cczu.nosql.entity.LikeHistory;
import com.cczu.nosql.entity.UserArticleLike;
import com.cczu.nosql.service.RedisService;
import io.ebean.DB;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ArticleLikeConsumer {

  /** 单线程后台消费者：负责将 Redis 队列中的点赞/取消点赞事件同步到 MySQL。 注意：这里只负责 MySQL 的最终状态，不做任何 Redis 回填或排行榜更新。 */
  private final ExecutorService consumerExecutor =
      Executors.newSingleThreadExecutor(
          r -> {
            Thread t = new Thread(r);
            t.setName("article-like-consumer");
            t.setDaemon(true);
            return t;
          });

  @Autowired private RedisService redisService;

  @PostConstruct
  public void start() {
    consumerExecutor.submit(this::runConsumeLoop);
  }

  /** 持续从 Redis 阻塞队列消费点赞事件，一条消息对应一次 MySQL 同步。 */
  private void runConsumeLoop() {
    String queueKey = RedisKeyConstant.getArticleLikeQueueKey();
    while (true) {
      try {
        // 阻塞式获取一条消息
        ArticleLikeMessage message = redisService.bQueueTake(queueKey, ArticleLikeMessage.class);
        if (message == null) {
          continue;
        }
        log.info("消费文章点赞消息: {}", message);
        handleMessage(message);
      } catch (Exception e) {
        // 消费端异常只记录日志，不中断循环
        log.error("文章点赞消息消费异常", e);
      }
    }
  }

  /**
   * 将一条点赞/取消点赞消息落库到 MySQL： 1. 根据 articleId 校验文章是否存在 2. 维护 UserArticleLike 关系（插入/删除） 3. 调整
   * Article.likeCount 计数 4. 写入 LikeHistory 记录
   */
  private void handleMessage(ArticleLikeMessage msg) {
    Long articleId = msg.getArticleId();
    Long userId = msg.getUserId();
    boolean isLike = msg.isLike();

    if (articleId == null || userId == null) {
      log.warn("收到非法点赞消息, articleId 或 userId 为空: {}", msg);
      return;
    }

    // 1. 查文章
    Article article = DB.find(Article.class, articleId);
    if (article == null) {
      log.warn("文章不存在, articleId={}", articleId);
      return;
    }

    // 2. 查现有用户-文章点赞关系
    UserArticleLike rel =
        DB.find(UserArticleLike.class)
            .where()
            .eq("userId", userId)
            .eq("articleId", articleId)
            .findOne();

    long current = article.getLikeCount() == null ? 0L : article.getLikeCount();

    if (isLike) {
      // 点赞：不存在则新增关系并使点赞数 +1
      if (rel == null) {
        rel = new UserArticleLike(userId, articleId);
        DB.save(rel);
        article.setLikeCount(current + 1);
        DB.update(article);
      } else {
        // 已存在关系则视为幂等，略过
        log.debug("重复点赞, userId={}, articleId={}", userId, articleId);
      }
    } else {
      // 取消点赞：存在则删除关系并使点赞数 -1
      if (rel != null) {
        DB.delete(rel);
        article.setLikeCount(Math.max(0L, current - 1));
        DB.update(article);
      } else {
        // 不存在关系则视为幂等取消
        log.debug("重复取消点赞, userId={}, articleId={}", userId, articleId);
      }
    }

    // 4. 写点赞历史记录
    LikeHistory history = new LikeHistory(userId, articleId, isLike);
    DB.save(history);
  }
}
