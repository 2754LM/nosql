package com.cczu.nosql.service.impl;

import com.cczu.nosql.constant.BizCode;
import com.cczu.nosql.entity.Article;
import com.cczu.nosql.exception.BizException;
import com.cczu.nosql.request.ArticleQueryParam;
import com.cczu.nosql.request.CreateArticleRequest;
import com.cczu.nosql.request.PageParam;
import com.cczu.nosql.request.UpdateArticleRequest;
import com.cczu.nosql.response.FullArticleResponse;
import com.cczu.nosql.service.ArticleCacheService;
import com.cczu.nosql.service.ArticleService;
import com.cczu.nosql.service.CacheService;
import io.ebean.DB;
import io.ebean.PagedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
	@Autowired
	ArticleCacheService articleCacheService;

	@Override
	public void create(CreateArticleRequest request) {
		Article article = new Article();
		article.setTitle(request.getTitle());
		article.setContent(request.getContent());
		DB.save(article);
		System.out.println(article);

	}

	@Override
	public List<FullArticleResponse> searchArticles(ArticleQueryParam queryParam, PageParam pageParam) {
		var expr = DB.find(Article.class).where();

		if (queryParam.getTitle() != null && !queryParam.getTitle().isBlank()) {
			expr.ilike("title", "%" + queryParam.getTitle().trim() + "%");
		}
		if (queryParam.getContent() != null && !queryParam.getContent().isBlank()) {
			expr.ilike("content", "%" + queryParam.getContent().trim() + "%");
		}
		if (queryParam.getAuthorId() != null) {
			expr.eq("author.id", queryParam.getAuthorId());
		}
		if (queryParam.getId() != null) {
			expr.eq("id", queryParam.getId());
		}
		if (queryParam.getStartDate() != null) {
			expr.ge("recTime", queryParam.getStartDate());
		}
		if (queryParam.getEndDate() != null) {
			expr.le("recTime", queryParam.getEndDate());
		}

		String orderBy = queryParam.getSort().getFieldName()
				+ (Boolean.TRUE.equals(queryParam.getDesc()) ? " desc" : " asc");

		PagedList<Article> pagedList = expr.orderBy(orderBy)
				.setFirstRow((int) ((pageParam.getCurrent() - 1) * pageParam.getSize()))
				.setMaxRows((int) pageParam.getSize())
				.findPagedList();
		List<FullArticleResponse> response = pagedList.getList().stream()
				.map(FullArticleResponse::new)
				.toList();
		for(FullArticleResponse cur : response){
			Long likeCount = articleCacheService.getLikeCount(cur.getId());
			cur.setLikeCount(likeCount);
		}
		return response;
	}

	@Override
	public void updateArticle(UpdateArticleRequest request) {
		Article article = DB.find(Article.class, request.getId());
		if (article == null) {
			throw new BizException(BizCode.ARTICLE_NOT_FOUND);
		}
		article.setTitle(request.getTitle());
		article.setContent(request.getContent());
		DB.update(article);
	}

}
