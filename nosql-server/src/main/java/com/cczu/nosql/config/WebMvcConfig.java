package com.cczu.nosql.config;

import com.cczu.nosql.interceptor.JwtAuthInterceptor;
import com.cczu.nosql.interceptor.SessionClearInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private final JwtAuthInterceptor jwtAuthInterceptor;
	private final SessionClearInterceptor sessionClearInterceptor;

	public WebMvcConfig(JwtAuthInterceptor jwtAuthInterceptor, SessionClearInterceptor sessionClearInterceptor) {
		this.jwtAuthInterceptor = jwtAuthInterceptor;
		this.sessionClearInterceptor = sessionClearInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//		 registry.addInterceptor(jwtAuthInterceptor).addPathPatterns("/api/**")
//				 .excludePathPatterns("/api/auth/**");
//		 registry.addInterceptor(sessionClearInterceptor).addPathPatterns("/api/**")
//				 .excludePathPatterns("/api/auth/**");
	}
}

