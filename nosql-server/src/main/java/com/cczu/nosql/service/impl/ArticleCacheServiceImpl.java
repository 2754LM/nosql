package com.cczu.nosql.service.impl;

import com.cczu.nosql.constant.RedisKeyConstant;
import com.cczu.nosql.entity.Article;
import com.cczu.nosql.service.ArticleCacheService;
import com.cczu.nosql.service.CacheService;
import io.ebean.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ArticleCacheServiceImpl implements ArticleCacheService {
	@Autowired
	CacheService cacheService;

	@Override
	public Long getLikeCount(Long id) {
		Long value = cacheService.getValue(RedisKeyConstant.getArticleLikeCountKey(id), Long.class);
		if(value != null) return value;
		value = DB.find(Article.class).setId(id).findOne().getLikeCount();
		cacheService.setValue(RedisKeyConstant.getArticleLikeCountKey(id), value);
		return value;
	}

	@Override
	public Article getArticle(Long articleId) {
		Article article = cacheService.getValue(RedisKeyConstant.getArticleKey(articleId), Article.class);
		if(article != null) return article;
		article = DB.find(Article.class).setId(articleId).findOne();
		cacheService.setValue(RedisKeyConstant.getArticleKey(articleId), article);
		Long likeCount = getLikeCount(articleId);
		article.setLikeCount(likeCount);
		return article;
	}


}
