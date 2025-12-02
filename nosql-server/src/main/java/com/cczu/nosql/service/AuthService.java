package com.cczu.nosql.service;

import com.cczu.nosql.entity.User;
import com.cczu.nosql.request.LoginRequest;
import com.cczu.nosql.request.RegisterRequest;
import jakarta.validation.Valid;

public interface AuthService {
	User login(LoginRequest dto);

	User register(@Valid RegisterRequest dto);
}
