package com.gkttk.monitoring.controllers.rest;

import com.gkttk.monitoring.models.dtos.JwtRequest;
import com.gkttk.monitoring.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthRestController {

  private final JwtService jwtService;

  @PostMapping
  public String createAuthToken(@RequestBody JwtRequest request) {

    return jwtService.createJwtToken(request);
  }
}
