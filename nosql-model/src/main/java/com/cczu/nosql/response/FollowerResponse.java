package com.cczu.nosql.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerResponse {
	// 粉丝
	@Schema(description = "粉丝")
	private UserInfoResponse follower;

	//是否互关
	@Schema(description = "是否互关")
	private boolean isMutual = false;

}
