package com.cczu.nosql.service;

import com.cczu.nosql.entity.User;
import com.cczu.nosql.dto.UserLoginDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

public interface UserService {
	public User login(UserLoginDto dto);

	User register(@Valid UserLoginDto dto);
}
