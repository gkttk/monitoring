package com.gkttk.monitoring.controllers.graphql;

import com.gkttk.monitoring.exceptions.AuthException;
import com.gkttk.monitoring.models.dtos.AuthUserData;
import com.gkttk.monitoring.models.dtos.JwtRequest;
import com.gkttk.monitoring.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthGraphQLController {

  private final JwtService jwtService;

  @QueryMapping
  public AuthUserData auth(@Argument JwtRequest authRequest) {
    return jwtService.createJwtToken(authRequest);
  }

  @QueryMapping
  public AuthUserData validate(@Argument AuthUserData data) {
    return jwtService.validateAuthUserData(data);
  }
}
