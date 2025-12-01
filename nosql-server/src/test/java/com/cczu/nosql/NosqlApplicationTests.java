package com.cczu.nosql;

import com.cczu.nosql.properties.JwtProperties;
import com.cczu.nosql.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class NosqlApplicationTests {
	@Autowired
	private JwtProperties props;
	@Test
	void jwtTest(){
		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", 1);
		claims.put("username", "admin");
		String jwt = JwtUtil.createJWT(props.getSecret(), props.getExpire(),  claims);
		Claims claims1 = JwtUtil.parseJWT(props.getSecret(), jwt);
		System.out.println(claims);
	}

}
