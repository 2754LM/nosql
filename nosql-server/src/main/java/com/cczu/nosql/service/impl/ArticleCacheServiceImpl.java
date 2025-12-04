package com.cczu.nosql.service.impl;

import com.cczu.nosql.constant.RedisKeyConstant;
import com.cczu.nosql.entity.Article;
import com.cczu.nosql.service.ArticleCacheService;
import com.cczu.nosql.service.CacheService;
import io.ebean.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
		cacheService.zAdd(RedisKeyConstant.getArticleLikeRankingKey(), value.doubleValue(), id);
		return value;
	}

	@Override
	public List<Article> getHotArticles(int limit) {
		return cacheService.zRange( RedisKeyConstant.getArticleLikeRankingKey(), 0, limit - 1, true, Article.class);
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

	@Override
	public Map<Long, Long> batchGetLikeCount(List<Long> articleIds) {
		if(articleIds == null || articleIds.isEmpty()) {
			return Map.of();
		}
		List<String> cacheKeys = articleIds.stream()
				.map(RedisKeyConstant::getArticleLikeCountKey)
				.toList();
		Map<String, Long> cachedValues = cacheService.multiGet(cacheKeys, Long.class);
		Map<Long, Long> result = new HashMap<>();
		List<Long> missingIds = new ArrayList<>();
		for (int i = 0; i < articleIds.size(); i++) {
			Long articleId = articleIds.get(i);
			String cacheKey = cacheKeys.get(i);
			Long cachedValue = cachedValues.get(cacheKey);
			if (cachedValue != null) {
				result.put(articleId, cachedValue);
			} else {
				missingIds.add(articleId);
			}
		}
		if (!missingIds.isEmpty()) {
			List<Article> articles = DB.find(Article.class)
					.where().idIn(missingIds)
					.findList();
			Map<Long, Long> dbResults = new HashMap<>();
			for (Article article : articles) {
				Long likeCount = article.getLikeCount();
				dbResults.put(article.getId(), likeCount);
			}
			Map<String, Object> cacheUpdates = new HashMap<>();
			for (Long articleId : missingIds) {
				Long likeCount = dbResults.get(articleId);
				if (likeCount != null) {
					result.put(articleId, likeCount);
					cacheUpdates.put(RedisKeyConstant.getArticleLikeCountKey(articleId), likeCount);
				}
			}
			cacheService.multiSet(cacheUpdates);
		}
		return result;
	}


	@Override
	public Map<Long, Article> batchGetArticle(List<Long> articleIds) {
		return Map.of();
	}

}
