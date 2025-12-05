package com.cczu.nosql.interceptor;

import com.cczu.nosql.constant.JwtClaimsConstant;
import com.cczu.nosql.properties.JwtProperties;
import com.cczu.nosql.util.JwtUtil;
import com.cczu.nosql.util.SessionContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {
  private final JwtProperties jwtProps;

  public JwtAuthInterceptor(JwtProperties jwtProps) {
    this.jwtProps = jwtProps;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    String token = request.getHeader(jwtProps.getHeader());
    if (token == null || token.isBlank()) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Missing token");
      return false;
    }

    try {
      Claims claims = JwtUtil.parseJWT(jwtProps.getSecret(), token);
      SessionContext.setUid(((Number) claims.get(JwtClaimsConstant.USER_ID)).longValue());
      return true;
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write(e.getMessage());
      return false;
    }
  }
}
