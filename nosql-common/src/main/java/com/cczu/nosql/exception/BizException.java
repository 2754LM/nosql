package com.cczu.nosql.exception;

import com.cczu.nosql.constant.BizCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BizException extends RuntimeException {
  private final int code;

  public BizException(BizCode bizCode) {
    super(bizCode.getMessage());
    this.code = bizCode.getCode();
  }
}
