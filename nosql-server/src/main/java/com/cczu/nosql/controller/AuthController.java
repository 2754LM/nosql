package com.cczu.nosql.controller;

import com.cczu.nosql.request.LoginRequest;
import com.cczu.nosql.request.RegisterRequest;
import com.cczu.nosql.response.LoginResponse;
import com.cczu.nosql.response.RegisterResponse;
import com.cczu.nosql.result.Result;
import com.cczu.nosql.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 认证控制器 */
@Tag(name = "认证控制器", description = "认证控制器")
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  /**
   * 用户登录
   *
   * @param request 登录请求
   * @return 登录响应
   */
  @Operation(summary = "用户登录", description = "用户登录")
  @PostMapping("/login")
  public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    log.info("用户登录，用户名：{}", request.getUsername());
    LoginResponse response = userService.login(request);
    log.info("用户登录成功，{}", response);
    return Result.success(response);
  }

  /**
   * 用户注册
   *
   * @param request 注册请求
   * @return 注册响应
   */
  @Operation(summary = "用户注册", description = "用户注册")
  @PostMapping("/register")
  public Result<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
    log.info("用户注册，用户名：{}", request.getUsername());
    RegisterResponse response = userService.register(request);
    log.info("用户注册成功，{}", response);
    return Result.success(response);
  }
}
