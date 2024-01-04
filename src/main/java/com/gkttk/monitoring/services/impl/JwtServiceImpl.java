package com.gkttk.monitoring.services.impl;

import com.gkttk.monitoring.components.util.JwtTokenGenerator;
import com.gkttk.monitoring.exceptions.AuthException;
import com.gkttk.monitoring.models.dtos.JwtRequest;
import com.gkttk.monitoring.services.JwtService;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  private final UserDetailsService userDetailsService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenGenerator tokenGenerator;

  @Override
  public String createJwtToken(JwtRequest request) {
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

    return tokenGenerator.generateToken(username, rolesList);
  }
}
