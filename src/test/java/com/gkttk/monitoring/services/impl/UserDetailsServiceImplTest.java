package com.gkttk.monitoring.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gkttk.monitoring.dao.repositories.UserRepository;
import com.gkttk.monitoring.models.entities.Role;
import com.gkttk.monitoring.models.entities.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

  private static final Long ID = 1L;
  private static final String LOGIN = "test_login";
  private static final String PASSWORD = "test_password";
  private static Set<Role> ROLES;

  @Mock private UserRepository repository;

  @InjectMocks private UserDetailsServiceImpl service;

  @BeforeAll
  static void beforeAll() {

    Role role1 = new Role();
    role1.setId(1L);
    role1.setName("ROLE_1");

    Role role2 = new Role();
    role2.setId(2L);
    role2.setName("ROLE_2");
    ROLES = Set.of(role1, role2);
  }

  @Test
  public void testLoadUserByUsernameShouldRequestUserIfTheUserWithProvidedLoginExistsInTheDb() {
    // given
    User expectedUser = new User();
    expectedUser.setId(ID);
    expectedUser.setLogin(LOGIN);
    expectedUser.setPassword(PASSWORD);
    expectedUser.setRoles(ROLES);
    when(repository.findByLogin(LOGIN)).thenReturn(Optional.of(expectedUser));

    // when
    UserDetails result = service.loadUserByUsername(LOGIN);
    // then
    System.out.println();
    assertEquals(LOGIN, result.getUsername());
    assertEquals(PASSWORD, result.getPassword());
    List<String> authorities =
        result.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

    ROLES.stream()
        .map(Role::getName)
        .forEach(roleValue -> assertTrue(authorities.contains(roleValue)));

    verify(repository).findByLogin(LOGIN);
  }

  @Test
  public void
      testLoadUserByUsernameShouldThrowExceptionIfTheUserWithProvidedLoginDoesNotExistInTheDb() {
    // given
    when(repository.findByLogin(LOGIN)).thenReturn(Optional.empty());
    // when
    assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(LOGIN));
    // then
    verify(repository).findByLogin(LOGIN);
  }
}
