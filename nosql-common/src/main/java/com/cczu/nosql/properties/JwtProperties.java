package com.cczu.nosql.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
	private String secret;
	private Duration expire;
	private String header;
}
