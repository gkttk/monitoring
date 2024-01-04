package com.gkttk.monitoring.services;

import com.gkttk.monitoring.models.dtos.JwtRequest;

public interface JwtService {

  String createJwtToken(JwtRequest request);
}
