package com.cczu.nosql.service;

public interface LikeService {
  void likeArticle(Long articleId);

  Boolean isArticleLiked(Long articleId);
}
