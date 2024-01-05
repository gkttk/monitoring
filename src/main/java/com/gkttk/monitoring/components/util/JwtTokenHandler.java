package com.gkttk.monitoring.components.util;

import io.jsonwebtoken.*;
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
public class JwtTokenHandler {

  private static final String CLAIMS_ROLES_KEY = "roles";

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.lifetime}")
  private Duration jwtLifetime;

  public String generateToken(String username, List<String> rolesList) {

    Map<String, Object> claims = new HashMap<>();
    claims.put(CLAIMS_ROLES_KEY, rolesList);

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

  public String getSubject(String token) {
    return getBody(token).getSubject();
  }

  @SuppressWarnings("unchecked")
  public List<String> getRoles(String token) {
    return getBody(token).get(CLAIMS_ROLES_KEY, List.class);
  }

  private Claims getBody(String token) throws JwtException, IllegalArgumentException {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }
}
