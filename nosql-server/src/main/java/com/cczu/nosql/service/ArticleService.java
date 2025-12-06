package com.cczu.nosql.service;

import com.cczu.nosql.request.ArticleQueryParam;
import com.cczu.nosql.request.CreateArticleRequest;
import com.cczu.nosql.request.PageParam;
import com.cczu.nosql.request.UpdateArticleRequest;
import com.cczu.nosql.response.FullArticleResponse;
import java.util.List;

public interface ArticleService {
  void create(CreateArticleRequest request);

  List<FullArticleResponse> searchArticles(ArticleQueryParam queryParam, PageParam pageParam);

  void updateArticle(Long id, UpdateArticleRequest request);

  void deleteArticle(Long id);

  List<FullArticleResponse> searchArticles(int limit);
}
