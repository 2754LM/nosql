package com.cczu.nosql.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema
@Data
@AllArgsConstructor
public class LoginResponse {
  // 用户ID
  @Schema(description = "用户ID")
  private Long userId;

  // 用户名
  @Schema(description = "用户名")
  private String username;

  // 生成的JWT令牌
  @Schema(description = "生成的JWT令牌")
  private String token;
}
