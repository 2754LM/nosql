package com.cczu.nosql.service;

import com.cczu.nosql.request.ArticleQueryParam;
import com.cczu.nosql.request.CreateArticleRequest;
import com.cczu.nosql.request.PageParam;
import com.cczu.nosql.request.UpdateArticleRequest;
import com.cczu.nosql.response.FullArticleResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface ArticleService {
	void create(@Valid CreateArticleRequest request);

	List<FullArticleResponse> searchArticles(@Valid ArticleQueryParam queryParam, PageParam pageParam);

	void updateArticle(@Valid UpdateArticleRequest request);
}
