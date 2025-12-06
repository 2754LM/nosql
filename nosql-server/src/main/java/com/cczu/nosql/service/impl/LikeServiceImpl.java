package com.cczu.nosql.service.impl;

import com.cczu.nosql.service.ArticleCacheService;
import com.cczu.nosql.service.LikeService;
import com.cczu.nosql.util.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {
  @Autowired ArticleCacheService articleCacheService;

  @Override
  public void likeArticle(Long articleId) {
    Long userId = SessionContext.getSession().getUserId();
    if (articleCacheService.isLikedArticle(articleId, userId)) {
      articleCacheService.unlikeArticle(articleId, userId);
    } else {
      articleCacheService.likeArticle(articleId, userId);
    }
  }

  @Override
  public Boolean isArticleLiked(Long articleId) {
    return articleCacheService.isLikedArticle(articleId, SessionContext.getSession().getUserId());
  }
}
