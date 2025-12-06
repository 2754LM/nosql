package com.cczu.nosql.service.impl;

import com.cczu.nosql.constant.RedisKeyConstant;
import com.cczu.nosql.entity.Article;
import com.cczu.nosql.message.ArticleLikeMessage;
import com.cczu.nosql.message.ArticleLikeProducer;
import com.cczu.nosql.service.ArticleCacheService;
import com.cczu.nosql.service.RedisService;
import java.util.*;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleCacheServiceImpl implements ArticleCacheService {

  @Autowired private RedisService cacheService;

  @Autowired private ArticleLikeProducer articleLikeProducer;

  /* ----------------------- 基础通用工具方法 ----------------------- */

  /** 将 id 列表转换为 redis key 列表 */
  private <ID> List<String> toKeys(List<ID> ids, Function<ID, String> keyFunc) {
    if (ids == null || ids.isEmpty()) {
      return List.of();
    }
    return ids.stream().map(keyFunc).toList();
  }

  /** 通用批量 get：id -> key -> value 的映射 */
  private <ID, V> Map<ID, V> batchGet(List<ID> ids, Function<ID, String> keyFunc, Class<V> clazz) {
    if (ids == null || ids.isEmpty()) {
      return Map.of();
    }
    List<String> keys = toKeys(ids, keyFunc);
    Map<String, V> cached = cacheService.mGet(keys, clazz);
    Map<ID, V> result = new HashMap<>(ids.size());
    for (int i = 0; i < ids.size(); i++) {
      ID id = ids.get(i);
      String key = keys.get(i);
      result.put(id, cached.get(key));
    }
    return result;
  }

  /** 通用批量 set：id,value -> key,value 映射 */
  private <ID, V> void batchSet(Map<ID, V> data, Function<ID, String> keyFunc) {
    if (data == null || data.isEmpty()) {
      return;
    }
    Map<String, V> cacheMap = new HashMap<>(data.size());
    for (Map.Entry<ID, V> entry : data.entrySet()) {
      cacheMap.put(keyFunc.apply(entry.getKey()), entry.getValue());
    }
    cacheService.mSet(cacheMap);
  }

  /** 点赞排行榜 key */
  private String likeRankingKey() {
    return RedisKeyConstant.getArticleLikeRankingKey();
  }

  /** 文章缓存 key */
  private String articleKey(Long id) {
    return RedisKeyConstant.getArticleKey(id);
  }

  /** 点赞数缓存 key */
  private String likeCountKey(Long id) {
    return RedisKeyConstant.getArticleLikeCountKey(id);
  }

  /** 点赞用户集合 key */
  private String likedUsersKey(Long articleId) {
    return RedisKeyConstant.getArticleLikedUsersKey(articleId);
  }

  /* ----------------------- 点赞数缓存相关 ----------------------- */

  @Override
  public Long getLikeCount(Long id) {
    return cacheService.get(likeCountKey(id), Long.class);
  }

  @Override
  public void setLikeCount(Long articleId, Long likeCount) {
    cacheService.set(likeCountKey(articleId), likeCount);
    cacheService.zAdd(likeRankingKey(), likeCount.doubleValue(), articleId.toString());
  }

  @Override
  public void addLikeCount(Long articleId, Long delta) {
    cacheService.incrBy(likeCountKey(articleId), delta);
    cacheService.zIncrBy(likeRankingKey(), delta.doubleValue(), articleId.toString());
  }

  @Override
  public Map<Long, Long> batchGetLikeCount(List<Long> articleIds) {
    return batchGet(articleIds, this::likeCountKey, Long.class);
  }

  @Override
  public void batchSetLikeCount(Map<Long, Long> likeCountMap) {
    batchSet(likeCountMap, this::likeCountKey);
    // 同步更新排行榜
    for (Map.Entry<Long, Long> entry : likeCountMap.entrySet()) {
      Long articleId = entry.getKey();
      Long likeCount = entry.getValue();
      cacheService.zAdd(likeRankingKey(), likeCount.doubleValue(), articleId.toString());
    }
  }

  @Override
  public void deleteArticleLikeCountCache(Long articleId) {
    cacheService.del(likeCountKey(articleId));
  }

  /* ----------------------- 热门文章排行榜 ----------------------- */

  @Override
  public List<Article> getHotArticles(int limit) {
    if (limit <= 0) {
      return List.of();
    }
    return cacheService.zRangeByIndex(likeRankingKey(), 0, limit - 1, true, Article.class);
  }

  @Override
  public void setHotArticles(List<Article> articles) {
    cacheService.del(likeRankingKey());
    if (articles == null || articles.isEmpty()) {
      return;
    }
    // member -> score
    Map<String, Double> scoreMembers = new HashMap<>(articles.size());
    for (Article article : articles) {
      Long likeCount = article.getLikeCount() == null ? 0L : article.getLikeCount();
      scoreMembers.put(article.getId().toString(), likeCount.doubleValue());
    }
    cacheService.zAddAll(likeRankingKey(), scoreMembers);
  }

  /* ----------------------- 文章实体缓存 ----------------------- */

  @Override
  public Article getArticle(Long articleId) {
    return cacheService.get(articleKey(articleId), Article.class);
  }

  @Override
  public void setArticle(Article article) {
    if (article == null || article.getId() == null) {
      return;
    }
    cacheService.set(articleKey(article.getId()), article);
  }

  @Override
  public Map<Long, Article> batchGetArticle(List<Long> articleIds) {
    return batchGet(articleIds, this::articleKey, Article.class);
  }

  @Override
  public void batchSetArticle(Map<Long, Article> articleMap) {
    batchSet(articleMap, this::articleKey);
  }

  @Override
  public void deleteArticleCache(Long articleId) {
    cacheService.del(articleKey(articleId));
    cacheService.del(likeCountKey(articleId));
    cacheService.del(likedUsersKey(articleId));
    cacheService.zRem(likeRankingKey(), articleId.toString());
  }

  @Override
  public void deleteArticleBaseCache(Long articleId) {
    cacheService.del(articleKey(articleId));
  }

  /* ----------------------- 点赞用户集合 ----------------------- */

  @Override
  public boolean isLikedArticle(Long articleId, Long userId) {
    return cacheService.sIsMember(likedUsersKey(articleId), userId.toString());
  }

  @Override
  public void likeArticle(Long articleId, Long userId) {
    // 1\. 缓存层自增 + 记录点赞用户
    addLikeCount(articleId, 1L);
    cacheService.sAdd(likedUsersKey(articleId), userId.toString());
    ArticleLikeMessage event = new ArticleLikeMessage();
    event.setEventId(java.util.UUID.randomUUID().toString());
    event.setArticleId(articleId);
    event.setUserId(userId);
    event.setLike(true);
    event.setTimestamp(System.currentTimeMillis());
    articleLikeProducer.publish(event);
  }

  @Override
  public void unlikeArticle(Long articleId, Long userId) {
    // 1\. 缓存层自减 + 移除点赞用户
    addLikeCount(articleId, -1L);
    cacheService.sRem(likedUsersKey(articleId), userId.toString());
    ArticleLikeMessage event = new ArticleLikeMessage();
    event.setEventId(java.util.UUID.randomUUID().toString());
    event.setArticleId(articleId);
    event.setUserId(userId);
    event.setLike(false);
    event.setTimestamp(System.currentTimeMillis());
    articleLikeProducer.publish(event);
  }
}
