package com.cczu.nosql.controller;

import com.cczu.nosql.constant.JwtClaimsConstant;
import com.cczu.nosql.request.LoginRequest;
import com.cczu.nosql.entity.User;
import com.cczu.nosql.properties.JwtProperties;
import com.cczu.nosql.result.Result;
import com.cczu.nosql.service.AuthService;
import com.cczu.nosql.util.JwtUtil;
import com.cczu.nosql.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
/**
 * 认证控制器
 */
@Tag(name = "认证控制器", description = "认证控制器")
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthService authService;
	@Autowired
	JwtProperties jwtProperties;

	/**
	 * 用户登录
	 * @param request 登录请求
	 * @return 登录响应
	 */
	@Operation(summary = "用户登录", description = "用户登录")
	@PostMapping("/login")
	public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
		log.info("用户登录，用户名：{}", request.getName());
		User user = authService.login(request);
		String token = JwtUtil.createJWT(
				jwtProperties.getSecret(),
				jwtProperties.getExpire(),
				Map.of(
						JwtClaimsConstant.USER_ID,   user.getId(),
						JwtClaimsConstant.USER_NAME, user.getName()
				));
		log.info("用户登录成功，用户名：{}，生成Token：{}", request.getName(), token);
		return Result.success(new LoginResponse(user.getId(), user.getName(), token));
	}

	/**
	 * 用户注册
	 * @param request 注册请求
	 * @return 注册响应
	 */
	@Operation(summary = "用户注册", description = "用户注册")
	@PostMapping("/register")
	public Result<LoginResponse> register(@Valid @RequestBody LoginRequest request) {
		log.info("用户注册，用户名：{}", request.getName());
		User user = authService.register(request);
		String token = JwtUtil.createJWT(
				jwtProperties.getSecret(),
				jwtProperties.getExpire(),
				Map.of(
						JwtClaimsConstant.USER_ID,   user.getId(),
						JwtClaimsConstant.USER_NAME, user.getName()
				));
		log.info("用户注册成功，用户名：{}，生成Token：{}", request.getName(), token);
		return Result.success(new LoginResponse(user.getId(), user.getName(), token));
	}

}
