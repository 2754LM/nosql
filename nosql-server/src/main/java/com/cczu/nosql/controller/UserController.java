package com.cczu.nosql.controller;

import com.cczu.nosql.entity.User;
import com.cczu.nosql.response.UserInfoResponse;
import com.cczu.nosql.result.Result;
import com.cczu.nosql.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@Tag(name = "用户控制器", description = "用户控制器")
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	UserService userService;
	@GetMapping("/{userId}")
	Result<UserInfoResponse> getUserInfo(@PathVariable Long userId) {
		log.info("查询用户信息，用户ID：{}", userId);
		User user = userService.getById(userId);
		return Result.success(new UserInfoResponse(user.getId(), user.getName()));
	}
}
