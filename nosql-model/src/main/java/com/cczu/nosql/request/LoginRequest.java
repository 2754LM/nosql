package com.cczu.nosql.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Schema
@Data
public class LoginRequest {
	//用户名
	@Schema(description = "用户名")
	@NotBlank(message = "用户名不能为空")
	private String name;
	//密码
	@Schema(description = "密码")
	@NotBlank(message = "密码不能为空")
	private String password;
}
