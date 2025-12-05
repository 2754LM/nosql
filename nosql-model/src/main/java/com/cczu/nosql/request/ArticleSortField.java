package com.cczu.nosql.request;

import lombok.Getter;

@Getter
public enum ArticleSortField {
  CREATE_TIME("crtTime"),
  UPDATE_TIME("recTime"),
  TITLE("title"),
  LIKE_COUNT("likeCount");

  private final String fieldName;

  ArticleSortField(String fieldName) {
    this.fieldName = fieldName;
  }
}
