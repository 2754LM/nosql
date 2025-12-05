// java
package com.cczu.nosql.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BizCode {
  // 1xxxx  通用
  SUCCESS(200, "成功"),
  PARAM_INVALID(1001, "参数非法"),
  SYSTEM_ERROR(1002, "系统异常"),
  DATA_NOT_FOUND(1003, "数据未找到"),
  DATA_ALREADY_EXIST(1004, "数据已存在"),
  OPERATION_FAILED(1005, "操作失败"),
  PARAM_TYPE_ERROR(1006, "参数类型错误"),
  PARAM_NOT_NULL(1007, "参数不能为空"),
  PATH_NOT_FOUND(1008, "请求路径不存在"),

  // 2xxxx  业务
  USER_NOT_FOUND(2001, "用户不存在"),
  PASSWORD_WRONG(2002, "密码错误"),
  TOKEN_EXPIRED(2003, "登录已过期"),
  NO_PERMISSION(2005, "权限不足"),
  USER_EXIST(2006, "用户已存在"),
  ARTICLE_NOT_FOUND(2007, "文章不存在"),

  // 3xxxx  数据库
  DB_ERROR(3001, "数据库异常"),

  // 9999  未知错误
  UNKNOWN_ERROR(9999, "未知错误");

  private final int code;
  private final String message;
}
