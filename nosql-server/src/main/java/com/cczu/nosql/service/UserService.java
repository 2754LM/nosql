package com.cczu.nosql.service;

import com.cczu.nosql.request.LoginRequest;
import com.cczu.nosql.request.RegisterRequest;
import com.cczu.nosql.response.LoginResponse;
import com.cczu.nosql.response.RegisterResponse;
import com.cczu.nosql.response.UserInfoResponse;

public interface UserService {
	UserInfoResponse getInfoById(Long id);

	LoginResponse login(LoginRequest request);

	RegisterResponse register(RegisterRequest request);

}
