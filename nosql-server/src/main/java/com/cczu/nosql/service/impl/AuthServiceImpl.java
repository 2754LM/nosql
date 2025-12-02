package com.cczu.nosql.service.impl;

import com.cczu.nosql.constant.BizCode;
import com.cczu.nosql.entity.User;
import com.cczu.nosql.request.LoginRequest;
import com.cczu.nosql.exception.BizException;
import com.cczu.nosql.request.RegisterRequest;
import com.cczu.nosql.service.AuthService;
import com.cczu.nosql.service.UserService;
import io.ebean.DB;
import io.ebean.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
	UserService userService;
	@Override
	public User login(LoginRequest dto) {
		User user = userService.getByName(dto.getName());
		if(user == null){
			throw new BizException(BizCode.USER_NOT_FOUND);
		}
		if(!Objects.equals(user.getPassword(), dto.getPassword())){
			throw new BizException(BizCode.PASSWORD_WRONG);
		}
		return user;
	}

	@Override
	@Transactional
	public User register(RegisterRequest dto) {
		User existingUser = userService.getByName(dto.getName());
		if(existingUser != null){
			throw new BizException(BizCode.USER_EXIST);
		}
		User newUser = new User();
		newUser.setName(dto.getName());
		newUser.setPassword(dto.getPassword());
		userService.save(newUser);
		return newUser;
	}

}
