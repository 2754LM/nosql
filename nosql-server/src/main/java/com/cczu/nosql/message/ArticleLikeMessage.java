package com.cczu.nosql.message;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleLikeMessage implements Serializable {
  private String eventId;
  private Long articleId;
  private Long userId;
  private boolean isLike;
  private Long timestamp;
}
