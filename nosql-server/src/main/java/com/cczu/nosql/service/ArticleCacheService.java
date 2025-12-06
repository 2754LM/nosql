package com.cczu.nosql.service;

import com.cczu.nosql.entity.Article;
import java.util.List;
import java.util.Map;

/** 文章相关的缓存服务抽象。 主要负责文章实体、点赞数、热门排行、点赞用户集合等在 Redis 中的读写。 */
public interface ArticleCacheService {

  /**
   * 根据文章 ID 从缓存中获取文章详情。
   *
   * @param articleId 文章 ID
   * @return 文章实体，若不存在则返回 \`null\`
   */
  Article getArticle(Long articleId);

  /**
   * 将文章实体写入缓存。
   *
   * @param article 文章实体（必须包含 ID）
   */
  void setArticle(Article article);

  /**
   * 获取文章当前点赞数（从缓存中）。
   *
   * @param articleId 文章 ID
   * @return 点赞数，若缓存不存在通常返回 \`null\`
   */
  Long getLikeCount(Long articleId);

  /**
   * 设置文章点赞数（覆盖写入缓存，并同步到排行榜）。
   *
   * @param articleId 文章 ID
   * @param likeCount 点赞数
   */
  void setLikeCount(Long articleId, Long likeCount);

  /**
   * 增减文章点赞数。
   *
   * @param articleId 文章 ID
   * @param delta 累加量，正数表示点赞，负数表示取消点赞
   */
  void addLikeCount(Long articleId, Long delta);

  /**
   * 获取热门文章列表（从缓存的排行榜中）。
   *
   * @param limit 返回条数上限
   * @return 热门文章列表（按点赞数倒序）
   */
  List<Article> getHotArticles(int limit);

  /**
   * 重建热门文章排行榜。
   *
   * @param articles 文章列表，将以文章的点赞数作为排行榜分数
   */
  void setHotArticles(List<Article> articles);

  /**
   * 批量获取多篇文章的点赞数。
   *
   * @param articleIds 文章 ID 列表
   * @return key 为文章 ID，value 为点赞数的映射
   */
  Map<Long, Long> batchGetLikeCount(List<Long> articleIds);

  /**
   * 批量写入多篇文章的点赞数。
   *
   * @param likeCountMap key 为文章 ID，value 为点赞数的映射
   */
  void batchSetLikeCount(Map<Long, Long> likeCountMap);

  /**
   * 批量获取文章实体。
   *
   * @param articleIds 文章 ID 列表
   * @return key 为文章 ID，value 为文章实体的映射
   */
  Map<Long, Article> batchGetArticle(List<Long> articleIds);

  /**
   * 批量写入文章实体。
   *
   * @param articleMap key 为文章 ID，value 为文章实体的映射
   */
  void batchSetArticle(Map<Long, Article> articleMap);

  /**
   * 删除单篇文章的所有缓存（包括文章实体、排行榜相关等）。
   *
   * @param articleId 文章 ID
   */
  void deleteArticleCache(Long articleId);

  /**
   * 删除文章基础信息缓存（只删除文章实体，不动点赞数、排行榜）。
   *
   * @param articleId 文章 ID
   */
  void deleteArticleBaseCache(Long articleId);

  /**
   * 删除文章点赞数缓存。
   *
   * @param articleId 文章 ID
   */
  void deleteArticleLikeCountCache(Long articleId);

  /**
   * 判断用户是否已点赞某篇文章。
   *
   * @param articleId 文章 ID
   * @param userId 用户 ID
   * @return \`true\` 已点赞，\`false\` 未点赞
   */
  boolean isLikedArticle(Long articleId, Long userId);

  /**
   * 点赞文章（包含点赞数自增、记录点赞用户、可选的异步消息）。
   *
   * @param articleId 文章 ID
   * @param userId 用户 ID
   */
  void likeArticle(Long articleId, Long userId);

  /**
   * 取消点赞文章（包含点赞数自减、移除点赞用户、异步消息）。
   *
   * @param articleId 文章 ID
   * @param userId 用户 ID
   */
  void unlikeArticle(Long articleId, Long userId);
}

