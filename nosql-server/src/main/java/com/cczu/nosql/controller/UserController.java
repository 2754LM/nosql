package com.cczu.nosql.controller;
import com.cczu.nosql.constant.JwtClaimsConstant;
import com.cczu.nosql.properties.JwtProperties;
import com.cczu.nosql.result.Result;
import com.cczu.nosql.entity.User;
import com.cczu.nosql.dto.UserLoginDto;
import com.cczu.nosql.service.UserService;
import com.cczu.nosql.util.JwtUtil;
import com.cczu.nosql.vo.UserLoginVo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	UserService userService;
	@Autowired
	JwtProperties jwtProperties;

	@PostMapping("/login")
	public Result<UserLoginVo> login(@Valid @RequestBody UserLoginDto dto) {
		log.info("用户登录，用户名：{}", dto.getName());
		User user = userService.login(dto);
		String token = JwtUtil.createJWT(
				jwtProperties.getSecret(),
				jwtProperties.getExpire(),
				Map.of(
						JwtClaimsConstant.USER_ID,   user.getId(),
						JwtClaimsConstant.USER_NAME, user.getName()
				));
		log.info("用户登录成功，用户名：{}，生成Token：{}", dto.getName(), token);
		return Result.success(new UserLoginVo(user.getId(), user.getName(), token));
	}

	@PostMapping("/register")
	public Result<UserLoginVo> register(@Valid @RequestBody UserLoginDto dto) {
		log.info("用户注册，用户名：{}", dto.getName());
		User user = userService.register(dto);
		String token = JwtUtil.createJWT(
				jwtProperties.getSecret(),
				jwtProperties.getExpire(),
				Map.of(
						JwtClaimsConstant.USER_ID,   user.getId(),
						JwtClaimsConstant.USER_NAME, user.getName()
				));
		log.info("用户注册成功，用户名：{}，生成Token：{}", dto.getName(), token);
		return Result.success(new UserLoginVo(user.getId(), user.getName(), token));
	}


}
