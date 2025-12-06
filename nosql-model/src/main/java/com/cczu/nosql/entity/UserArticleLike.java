package com.cczu.nosql.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rel_user_article_like")
public class UserArticleLike {
  private Long id;
  private Long userId;
  private Long articleId;

  public UserArticleLike(Long userId, Long articleId) {
    this.userId = userId;
    this.articleId = articleId;
  }
}
