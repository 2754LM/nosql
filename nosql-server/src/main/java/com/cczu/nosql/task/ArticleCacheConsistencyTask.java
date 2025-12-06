package com.cczu.nosql.task;

import com.cczu.nosql.entity.Article;
import com.cczu.nosql.service.ArticleCacheService;
import io.ebean.DB;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** 定时校验 MySQL 与 Redis 中的文章数据是否一致， 若点赞数差异过大或文章内容不一致，则以 MySQL 为准回填 Redis。 */
@Component
@Slf4j
public class ArticleCacheConsistencyTask {

  /** 差异阈值：当 Redis 与 MySQL 的点赞数差值绝对值超过该值时触发回填。 */
  private static final long LIKE_DIFF_THRESHOLD = 5L;
  private final ArticleCacheService articleCacheService;

  public ArticleCacheConsistencyTask(ArticleCacheService articleCacheService) {
    this.articleCacheService = articleCacheService;
  }

  /** 每 30 分钟执行一次文章缓存一致性校验任务。 */
  @Scheduled(cron = "${nosql.scheduling.article-cache-consistency-cron}")
  public void checkAndRepairArticleCache() {
    try {
      log.info("开始执行文章缓存一致性校验任务");

      // 简单示例：一次性查出所有文章（数据量大时建议分页处理）
      List<Article> allArticles = DB.find(Article.class).findList();
      if (allArticles.isEmpty()) {
        return;
      }

      // 1. 批量获取文章在 Redis 中的点赞数与实体
      Map<Long, Long> cacheLikeMap =
          articleCacheService.batchGetLikeCount(allArticles.stream().map(Article::getId).toList());
      Map<Long, Article> cacheArticleMap =
          articleCacheService.batchGetArticle(allArticles.stream().map(Article::getId).toList());

      // 2. 找出需要回填的文章
      Map<Long, Long> fixLikeCountMap = new HashMap<>();
      Map<Long, Article> fixArticleMap = new HashMap<>();

      for (Article dbArticle : allArticles) {
        Long articleId = dbArticle.getId();
        if (articleId == null) {
          continue;
        }

        // 比对点赞数
        long dbLike = dbArticle.getLikeCount() == null ? 0L : dbArticle.getLikeCount();
        long cacheLike = cacheLikeMap.getOrDefault(articleId, 0L);
        if (Math.abs(dbLike - cacheLike) > LIKE_DIFF_THRESHOLD) {
          fixLikeCountMap.put(articleId, dbLike);
        }

        // 比对基础内容（这里只做标题 + 内容的简单对比）
        Article cacheArticle = cacheArticleMap.get(articleId);
        boolean needFixArticle = false;
        if (cacheArticle == null) {
          needFixArticle = true;
        } else {
          String dbTitle = dbArticle.getTitle();
          String cacheTitle = cacheArticle.getTitle();
          String dbContent = dbArticle.getContent();
          String cacheContent = cacheArticle.getContent();
          if (!safeEquals(dbTitle, cacheTitle) || !safeEquals(dbContent, cacheContent)) {
            needFixArticle = true;
          }
        }
        if (needFixArticle) {
          fixArticleMap.put(articleId, dbArticle);
        }
      }

      // 3. 执行回填：以 MySQL 为准写回 Redis
      if (!fixLikeCountMap.isEmpty()) {
        articleCacheService.batchSetLikeCount(fixLikeCountMap);
        log.info("已回填点赞数到 Redis，条数={}", fixLikeCountMap.size());
      }
      if (!fixArticleMap.isEmpty()) {
        articleCacheService.batchSetArticle(fixArticleMap);
        log.info("已回填文章实体到 Redis，条数={}", fixArticleMap.size());
      }

      log.info("文章缓存一致性校验任务完成，需修复点赞={}，需修复文章实体={}", fixLikeCountMap.size(), fixArticleMap.size());

    } catch (Exception e) {
      log.error("文章缓存一致性校验任务执行异常", e);
    }
  }

  private boolean safeEquals(String a, String b) {
    if (a == null) {
      return b == null;
    }
    return a.equals(b);
  }
}
