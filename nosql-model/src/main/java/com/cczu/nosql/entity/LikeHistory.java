package com.cczu.nosql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Table(name = "act_like_history")
@NoArgsConstructor
@AllArgsConstructor
public class LikeHistory extends BaseModel {
  @Id private Long id;
  private Long userId;
  private Long ArticleId;
  private Boolean isLiked;

  public LikeHistory(Long userId, Long articleId, Boolean isLiked) {
    this.userId = userId;
    this.ArticleId = articleId;
    this.isLiked = isLiked;
  }
}
