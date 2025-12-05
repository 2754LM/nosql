package com.cczu.nosql.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;

public class JwtUtil {
  public static String createJWT(String secretKey, Duration duration, Map<String, Object> claims) {
    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    long expMillis = System.currentTimeMillis() + duration.toMillis();
    Date exp = new Date(expMillis);

    JwtBuilder builder =
        Jwts.builder().claims(claims).signWith(key, SignatureAlgorithm.HS256).expiration(exp);

    return builder.compact();
  }

  // 校验JWT
  public static Claims parseJWT(String secretKey, String token) {
    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }
}
