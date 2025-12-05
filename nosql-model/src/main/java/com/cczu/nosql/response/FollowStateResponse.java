package com.cczu.nosql.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowStateResponse {
  // 操作结束后，true表示已关注，false表示未关注
  @Schema(description = "操作结果，true表示已关注，false表示未关注")
  private boolean following;
}
