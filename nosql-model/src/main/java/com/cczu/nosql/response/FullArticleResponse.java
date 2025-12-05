package com.cczu.nosql.response;

import com.cczu.nosql.entity.Article;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class FullArticleResponse {
  private Long id;

  private String title;

  private String content;

  private UserInfoResponse author;

  private Long likeCount;

  public FullArticleResponse(Article article) {
    this.id = article.getId();
    this.title = article.getTitle();
    this.content = article.getContent();
    this.author = new UserInfoResponse(article.getAuthor());
    this.likeCount = article.getLikeCount();
  }
}
