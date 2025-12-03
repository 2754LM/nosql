package com.cczu.nosql.service.impl;

import com.cczu.nosql.entity.Article;
import com.cczu.nosql.request.CreateArticleRequest;
import com.cczu.nosql.service.ArticleService;
import io.ebean.DB;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {
	@Override
	public void save(CreateArticleRequest request) {
		Article article = new Article();
		article.setTitle(request.getTitle());
		article.setContent(request.getContent());
		DB.save(article);
		System.out.println(article);

	}
}
