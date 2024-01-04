package com.gkttk.monitoring.components.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.lifetime}")
  private Duration jwtLifetime;

  public String generateToken(String username, List<String> rolesList) {

    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", rolesList);

    Date issuedDate = new Date();
    Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(username)
        .setIssuedAt(issuedDate)
        .setExpiration(expiredDate)
        .signWith(SignatureAlgorithm.HS256, secret)
        .compact();
  }
}
