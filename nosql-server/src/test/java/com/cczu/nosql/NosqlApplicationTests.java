package com.cczu.nosql;

import com.cczu.nosql.entity.User;
import com.cczu.nosql.properties.JwtProperties;
import com.cczu.nosql.util.JwtUtil;
import io.ebean.DB;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class NosqlApplicationTests {
	@Test
	void jwtTest(){
		User user = new User();
		user.setName("cczu");
		DB.save(user);
	}

}
