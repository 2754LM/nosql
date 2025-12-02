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
public class FollowingsResponse {
	// 用户ID
	@Schema(description = "用户ID")
	private Long userId;
	// 关注列表
	@Schema(description = "关注列表")
	private List<UserInfoResponse> followings;
}

