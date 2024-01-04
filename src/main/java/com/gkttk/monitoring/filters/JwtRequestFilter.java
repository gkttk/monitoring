package com.gkttk.monitoring.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
  private static final String AUTH_TOKEN_PREFIX = "Bearer ";
  private static final String ROLES_CLAIMS_KEY = "roles";

  @Value("${jwt.secret}")
  private String secret;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);
    String username = null;
    String jwt = null;

    if (authHeader != null && authHeader.startsWith(AUTH_TOKEN_PREFIX)) {
      jwt = authHeader.substring(7);
      try {
        username = extractUsername(jwt);
      } catch (ExpiredJwtException e) {
        log.debug("The current token is expired");
      } catch (SignatureException e) {
        log.debug("The provided signature is incorrect");
      }
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UsernamePasswordAuthenticationToken token =
          new UsernamePasswordAuthenticationToken(
              username, null, extractRoles(jwt).stream().map(SimpleGrantedAuthority::new).toList());
      SecurityContextHolder.getContext().setAuthentication(token);
    }

    filterChain.doFilter(request, response);
  }

  private String extractUsername(String token) {
    return getAllClaimsFromToken(token).getSubject();
  }

  private List<String> extractRoles(String token) {
    return getAllClaimsFromToken(token).get(ROLES_CLAIMS_KEY, List.class);
  }

  private Claims getAllClaimsFromToken(String token) {

    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }
}
