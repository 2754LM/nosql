package com.cczu.nosql.constant;

public class RedisKeyConstant {
	/**
	 * 文章点赞数缓存
	 * 数据类型：String
	 */
	private static final String ARTICLE_LIKE_COUNT = "article:like:count:";

	/**
	 * 文章基础信息缓存（不含点赞数）
	 * 数据类型：String
	 */
	private static final String ARTICLE_INFO = "article:info:";

	/**
	 * 文章点赞排行榜
	 * 数据类型：ZSet
	 */
	private static final String ARTICLE_RANKING_LIKES = "article:ranking:likes";


	public static String getArticleLikeCountKey(Long articleId) {
		return ARTICLE_LIKE_COUNT + articleId;
	}

	public static String getArticleKey(Long articleId) {
		return ARTICLE_INFO + articleId;
	}

	public static String getArticleLikeRankingKey() {
		return ARTICLE_RANKING_LIKES;
	}

}
