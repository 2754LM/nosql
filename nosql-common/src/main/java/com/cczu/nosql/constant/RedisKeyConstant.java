package com.cczu.nosql.constant;

public class RedisKeyConstant {
  /** 文章点赞数缓存 数据类型：String */
  private static final String ARTICLE_LIKE_COUNT = "article:like:count:";

  /** 文章基础信息缓存（不含点赞数） 数据类型：String */
  private static final String ARTICLE_INFO = "article:info:";

  /** 文章点赞排行榜 数据类型：ZSet */
  private static final String ARTICLE_RANKING_LIKES = "article:ranking:likes";

  /** 点赞过此文章的用户集合 数据类型：Set */
  private static final String ARTICLE_LIKED_USERS = "article:liked:users:";

  /*redis消息队列 key */
  private static final String ARTICLE_LIKE_QUEUE = "article:like:queue";

  /** JWT存储前缀 */
  private static final String JWT_KEY_PREFIX = "jwt:token:";

  public static String getArticleLikeCountKey(Long articleId) {
    return ARTICLE_LIKE_COUNT + articleId;
  }

  public static String getArticleKey(Long articleId) {
    return ARTICLE_INFO + articleId;
  }

  public static String getArticleLikeRankingKey() {
    return ARTICLE_RANKING_LIKES;
  }

  public static String getArticleLikedUsersKey(Long articleId) {
    return ARTICLE_LIKED_USERS + articleId;
  }

  public static String getArticleLikeQueueKey() {
    return ARTICLE_LIKE_QUEUE;
  }

  public static String getJwtKey(String token) {
    return JWT_KEY_PREFIX + token;
  }
}
