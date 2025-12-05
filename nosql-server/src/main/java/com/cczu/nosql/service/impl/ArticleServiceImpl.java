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
import com.cczu.nosql.util.SessionContext;
import io.ebean.DB;
import io.ebean.PagedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {
  @Autowired ArticleCacheService articleCacheService;

  @Override
  public void create(CreateArticleRequest request) {
    Article article = new Article();
    article.setTitle(request.getTitle());
    article.setContent(request.getContent());
    DB.save(article);
    articleCacheService.deleteArticleCache(article.getId());
  }

  @Override
  public List<FullArticleResponse> searchArticles(
      ArticleQueryParam queryParam, PageParam pageParam) {
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

    String orderBy =
        queryParam.getSort().getFieldName()
            + (Boolean.TRUE.equals(queryParam.getDesc()) ? " desc" : " asc");

    PagedList<Article> pagedList =
        expr.orderBy(orderBy)
            .setFirstRow((int) ((pageParam.getCurrent() - 1) * pageParam.getSize()))
            .setMaxRows((int) pageParam.getSize())
            .findPagedList();
    List<FullArticleResponse> response =
        pagedList.getList().stream().map(FullArticleResponse::new).toList();
    Map<Long, Long> likeCountMap =
        articleCacheService.batchGetLikeCount(
            response.stream().map(FullArticleResponse::getId).toList());
    for (FullArticleResponse cur : response) {
      cur.setLikeCount(likeCountMap.getOrDefault(cur.getId(), 0L));
    }
    return response;
  }

  @Override
  public void updateArticle(Long id, UpdateArticleRequest request) {
    request.setId(id);
    Article article = DB.find(Article.class, request.getId());
    if (article == null) {
      throw new BizException(BizCode.ARTICLE_NOT_FOUND);
    }
    article.setTitle(request.getTitle());
    article.setContent(request.getContent());
    DB.update(article);
  }

  @Override
  public void deleteArticle(Long id) {
    Article article = DB.find(Article.class, id);
    if (article == null) {
      throw new BizException(BizCode.ARTICLE_NOT_FOUND);
    }
    if (!Objects.equals(article.getAuthor().getId(), SessionContext.getSession().getUserId())) {
      throw new BizException(BizCode.NO_PERMISSION);
    }
    DB.delete(article);
    articleCacheService.deleteArticleCache(id);
  }
}
