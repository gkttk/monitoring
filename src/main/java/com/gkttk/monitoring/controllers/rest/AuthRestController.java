package com.gkttk.monitoring.controllers.rest;

import com.gkttk.monitoring.exceptions.AuthException;
import com.gkttk.monitoring.models.dtos.AuthUserData;
import com.gkttk.monitoring.models.dtos.JwtRequest;
import com.gkttk.monitoring.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthRestController {

  private final JwtService jwtService;

  @PostMapping
  public AuthUserData createAuthToken(@RequestBody JwtRequest request) throws AuthException {

    return jwtService.createJwtToken(request);
  }

  @PostMapping("/validate")
  public void validate(@RequestBody AuthUserData data) throws AuthException {

    jwtService.validateAuthUserData(data);
  }

  @ExceptionHandler(AuthException.class)
  public ResponseEntity<String> handleException(AuthException exception) {
    return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
  }
}
