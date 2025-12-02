package com.cczu.nosql.interceptor;

import com.cczu.nosql.util.SessionContext;
import org.springframework.web.servlet.HandlerInterceptor;

public class SessionClearInterceptor implements HandlerInterceptor {
	@Override
	public void afterCompletion(jakarta.servlet.http.HttpServletRequest request,
	                            jakarta.servlet.http.HttpServletResponse response,
	                            Object handler, Exception ex) {
		SessionContext.clear();
	}
}
