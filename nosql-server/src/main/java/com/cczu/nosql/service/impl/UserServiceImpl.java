package com.cczu.nosql.service.impl;

import com.cczu.nosql.constant.BizCode;
import com.cczu.nosql.constant.JwtClaimsConstant;
import com.cczu.nosql.entity.User;
import com.cczu.nosql.exception.BizException;
import com.cczu.nosql.properties.JwtProperties;
import com.cczu.nosql.request.LoginRequest;
import com.cczu.nosql.request.RegisterRequest;
import com.cczu.nosql.response.LoginResponse;
import com.cczu.nosql.response.RegisterResponse;
import com.cczu.nosql.response.UserInfoResponse;
import com.cczu.nosql.service.UserService;
import com.cczu.nosql.util.JwtUtil;
import io.ebean.DB;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  final JwtProperties jwtProperties;

  public UserServiceImpl(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
  }

  @Override
  public UserInfoResponse getInfoById(Long id) {
    User user = DB.find(User.class).where().eq("id", id).findOne();
    if (user == null) return new UserInfoResponse();
    return new UserInfoResponse(user);
  }

  @Override
  public LoginResponse login(LoginRequest request) {
    User user = DB.find(User.class).where().eq("username", request.getUsername()).findOne();
    if (user == null) throw new BizException(BizCode.USER_NOT_FOUND);
    if (!Objects.equals(user.getPassword(), request.getPassword()))
      throw new BizException(BizCode.PASSWORD_WRONG);
    String token =
        JwtUtil.createJWT(
            jwtProperties.getSecret(),
            jwtProperties.getExpire(),
            Map.of(
                JwtClaimsConstant.USER_ID, user.getId(),
                JwtClaimsConstant.USER_NAME, user.getName()));
    return new LoginResponse(user.getId(), user.getName(), token);
  }

  @Override
  public RegisterResponse register(RegisterRequest request) {
    User user = DB.find(User.class).where().eq("username", request.getUsername()).findOne();
    if (user != null) throw new BizException(BizCode.USER_EXIST);
    user = new User(request.getUsername(), request.getPassword());
    DB.save(user);
    String token =
        JwtUtil.createJWT(
            jwtProperties.getSecret(),
            jwtProperties.getExpire(),
            Map.of(
                JwtClaimsConstant.USER_ID, user.getId(),
                JwtClaimsConstant.USER_NAME, user.getName()));
    return new RegisterResponse(user.getId(), user.getName(), token);
  }
}
