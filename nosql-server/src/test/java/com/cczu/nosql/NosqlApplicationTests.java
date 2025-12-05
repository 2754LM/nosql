package com.cczu.nosql;

import com.cczu.nosql.entity.User;
import io.ebean.DB;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NosqlApplicationTests {
  @Test
  void jwtTest() {
    User user = new User();
    user.setName("cczu");
    DB.save(user);
  }
}
