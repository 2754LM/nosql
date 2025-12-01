package com.cczu.nosql.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BizCode {
	// 1xxxx  通用
	SUCCESS(200, "成功"),
	PARAM_INVALID(1001, "参数非法"),
	// 2xxxx  认证
	USER_NOT_FOUND(2001, "用户不存在"),
	PASSWORD_WRONG(2002, "密码错误"),
	TOKEN_EXPIRED(2003, "登录已过期"),
	USER_EXIST(2004, "用户已存在"),
	// 3xxxx  订单
	ORDER_NOT_EXIST(3001, "订单不存在"),
	ORDER_STATUS_ERROR(3002, "订单状态异常");

	private final int code;
	private final String message;
}
