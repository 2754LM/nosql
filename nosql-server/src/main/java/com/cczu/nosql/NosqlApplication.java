package com.cczu.nosql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NosqlApplication {

  public static void main(String[] args) {
    SpringApplication.run(NosqlApplication.class, args);
  }
}
