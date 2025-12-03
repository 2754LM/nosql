package com.cczu.nosql.service;

import com.cczu.nosql.request.CreateArticleRequest;
import jakarta.validation.Valid;

public interface ArticleService {
	void save(@Valid CreateArticleRequest request);
}
