package com.cczu.nosql.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema
@Data
public class LoginRequest {
  // 用户名
  @Schema(description = "用户名")
  @NotBlank(message = "用户名不能为空")
  @Size(max = 16, message = "用户名长度不能超过16个字符")
  private String username;

  // 密码
  @Schema(description = "密码")
  @NotBlank(message = "密码不能为空")
  @Max(value = 16, message = "密码长度不能超过16个字符")
  private String password;
}
