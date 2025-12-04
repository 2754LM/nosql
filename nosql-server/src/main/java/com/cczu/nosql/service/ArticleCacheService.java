package com.cczu.nosql.service;

import com.cczu.nosql.entity.Article;

import java.util.List;
import java.util.Map;

public interface ArticleCacheService {

	Article getArticle(Long articleId);
	Long getLikeCount(Long articleId);
	List<Article> getHotArticles(int limit);
	Map<Long, Long> batchGetLikeCount(List<Long> articleIds);
	Map<Long, Article> batchGetArticle(List<Long> articleIds);
}
