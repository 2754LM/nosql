package com.cczu.nosql.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginVo {
	private Long userId;
	private String userName;
	private String token;
}
