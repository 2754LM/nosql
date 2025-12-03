package com.cczu.nosql.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowingResponse {
	// 关注者
	@Schema(description = "关注者")
	private UserInfoResponse following;
	//是否互关
	@Schema(description = "是否互关")
	private boolean isMutual;
}

