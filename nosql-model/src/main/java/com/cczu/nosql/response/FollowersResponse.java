package com.cczu.nosql.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowersResponse {
	// 用户ID
	@Schema(description = "用户ID")
	private Long userId;
	// 粉丝列表
	@Schema(description = "粉丝列表")
	private List<UserInfoResponse> followers;
}
