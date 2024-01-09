package com.gkttk.monitoring.services;

import com.gkttk.monitoring.exceptions.AuthException;
import com.gkttk.monitoring.models.dtos.AuthUserData;
import com.gkttk.monitoring.models.dtos.JwtRequest;

public interface JwtService {

  AuthUserData createJwtToken(JwtRequest request) throws AuthException;

  AuthUserData validateAuthUserData(AuthUserData data) throws AuthException;
}
