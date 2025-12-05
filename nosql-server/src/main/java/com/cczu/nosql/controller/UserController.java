package com.cczu.nosql.controller;

import com.cczu.nosql.response.UserInfoResponse;
import com.cczu.nosql.result.Result;
import com.cczu.nosql.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 用户控制器 */
@Tag(name = "用户控制器", description = "用户控制器")
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
  @Autowired UserService userService;

  /**
   * 获取用户信息
   *
   * @param userId 用户ID
   * @return 用户信息
   */
  @Parameter(name = "userId", description = "用户ID", in = ParameterIn.PATH, required = true)
  @Operation(summary = "获取用户信息", description = "获取用户信息")
  @GetMapping("/{userId}")
  Result<UserInfoResponse> getUserInfo(@PathVariable Long userId) {
    log.info("查询用户信息，用户ID：{}", userId);
    UserInfoResponse response = userService.getInfoById(userId);
    return Result.success(response);
  }
}
