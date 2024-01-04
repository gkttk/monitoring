package com.gkttk.monitoring.services.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gkttk.monitoring.components.util.JwtTokenGenerator;
import com.gkttk.monitoring.exceptions.AuthException;
import com.gkttk.monitoring.models.dtos.JwtRequest;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTest {

  private static final String LOGIN = "login";
  private static final String PASSWORD = "password";
  @Mock private UserDetailsService userDetailsServiceMock;
  @Mock private AuthenticationManager authenticationManagerMock;
  @Mock private JwtTokenGenerator tokenGeneratorMock;
  @Mock private Authentication authenticationMock;
  @InjectMocks private JwtServiceImpl jwtService;

  @Test
  public void testCreateJwtTokenShouldCreateATokenIfProvidedLoginAndPasswordAreCorrect() {
    // given
    UsernamePasswordAuthenticationToken expectedAuthToken =
        new UsernamePasswordAuthenticationToken(LOGIN, PASSWORD);
    User expectedUser = new User(LOGIN, PASSWORD, Collections.emptyList());
    when(authenticationManagerMock.authenticate(eq(expectedAuthToken)))
        .thenReturn(authenticationMock);
    when(userDetailsServiceMock.loadUserByUsername(LOGIN)).thenReturn(expectedUser);
    when(tokenGeneratorMock.generateToken(LOGIN, Collections.emptyList())).thenReturn(anyString());
    JwtRequest request = JwtRequest.builder().login(LOGIN).password(PASSWORD).build();
    // when
    String randomResult = jwtService.createJwtToken(request);
    // then
    assertNotNull(randomResult);
    verify(authenticationManagerMock).authenticate(eq(expectedAuthToken));
    verify(userDetailsServiceMock).loadUserByUsername(LOGIN);
    verify(tokenGeneratorMock).generateToken(LOGIN, Collections.emptyList());
  }

  @Test
  public void testCreateJwtTokenShouldThrowAnExceptionIfProvidedLoginAndPasswordAreNotCorrect() {
    // given
    UsernamePasswordAuthenticationToken expectedAuthToken =
        new UsernamePasswordAuthenticationToken(LOGIN, PASSWORD);
    when(authenticationManagerMock.authenticate(eq(expectedAuthToken)))
        .thenThrow(BadCredentialsException.class);
    JwtRequest request = JwtRequest.builder().login(LOGIN).password(PASSWORD).build();
    // when
    AuthException thrownException =
        Assertions.assertThrows(AuthException.class, () -> jwtService.createJwtToken(request));
    // then
    Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), thrownException.getStatusCode());
    verify(authenticationManagerMock).authenticate(eq(expectedAuthToken));
  }
}
