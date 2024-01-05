package com.gkttk.monitoring.services.impl;

import com.gkttk.monitoring.components.util.JwtTokenHandler;
import com.gkttk.monitoring.exceptions.AuthException;
import com.gkttk.monitoring.models.dtos.AuthUserData;
import com.gkttk.monitoring.models.dtos.JwtRequest;
import com.gkttk.monitoring.services.JwtService;
import io.jsonwebtoken.JwtException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  private static final String ADMIN_ROLE_VALUE = "ROLE_ADMIN";
  private final UserDetailsService userDetailsService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenHandler tokenGenerator;

  @Override
  public AuthUserData createJwtToken(JwtRequest request) throws AuthException {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.login(), request.password()));
    } catch (BadCredentialsException e) {
      throw new AuthException("Incorrect login or password", HttpStatus.UNAUTHORIZED.value());
    }

    UserDetails userDetails = userDetailsService.loadUserByUsername(request.login());
    String username = userDetails.getUsername();
    List<String> rolesList =
        userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
    String token = tokenGenerator.generateToken(username, rolesList);

    return AuthUserData.builder()
        .login(username)
        .admin(hasAdminRole(rolesList))
        .token(token)
        .build();
  }

  @Override
  public void validateAuthUserData(AuthUserData data) throws AuthException {

    String token = data.token();
    try {
      String subject = tokenGenerator.getSubject(token);
      if (!subject.equals(data.login())) {
        throw new AuthException(
            "Provided auth data has incorrect login", HttpStatus.UNAUTHORIZED.value());
      }

      List<String> roles = tokenGenerator.getRoles(token);
      boolean isAdminInJwt = hasAdminRole(roles);
      if (isAdminInJwt != data.admin()) {
        throw new AuthException(
            "Provided auth data has incorrect is admin value", HttpStatus.UNAUTHORIZED.value());
      }

    } catch (JwtException | IllegalArgumentException exception) {
      throw new AuthException(
          "Provided auth data token is invalid", HttpStatus.UNAUTHORIZED.value());
    }
  }

  private boolean hasAdminRole(List<String> roles) {
    return roles.stream().anyMatch(role -> role.equals(ADMIN_ROLE_VALUE));
  }
}
