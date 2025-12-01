package com.cczu.nosql.service.impl;

import com.cczu.nosql.constant.BizCode;
import com.cczu.nosql.entity.User;
import com.cczu.nosql.dto.UserLoginDto;
import com.cczu.nosql.exception.BizException;
import com.cczu.nosql.service.UserService;
import io.ebean.DB;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
public class UserServiceImpl implements UserService {
	@Override
	public User login(UserLoginDto dto) {
		User user = DB.find(User.class).where().eq("name", dto.getName()).findOne();
		if(user == null){
			throw new BizException(BizCode.USER_NOT_FOUND);
		}
		if(!Objects.equals(user.getPassword(), dto.getPassword())){
			throw new BizException(BizCode.PASSWORD_WRONG);
		}
		return user;
	}

	@Override
	public User register(UserLoginDto dto) {
		User existingUser = DB.find(User.class).where().eq("name", dto.getName()).findOne();
		if(existingUser != null){
			throw new BizException(BizCode.USER_EXIST);
		}
		User newUser = new User();
		newUser.setName(dto.getName());
		newUser.setPassword(dto.getPassword());
		DB.save(newUser);
		return newUser;
	}

}
