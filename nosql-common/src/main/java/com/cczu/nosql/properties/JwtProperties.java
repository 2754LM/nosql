package com.cczu.nosql.properties;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
  private String secret;
  private Duration expire;
  private String header;
}
