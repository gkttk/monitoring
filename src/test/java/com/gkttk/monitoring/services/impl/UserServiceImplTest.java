package com.gkttk.monitoring.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gkttk.monitoring.components.mappers.UserMapper;
import com.gkttk.monitoring.dao.repositories.UserRepository;
import com.gkttk.monitoring.models.dtos.UserDto;
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

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

  private static final Long ID_1 = 1L;
  private static final String LOGIN_1 = "test_login_1";
  private static final String PASSWORD_1 = "test_password_1";
  private static final Long ID_2 = 2L;
  private static final String LOGIN_2 = "test_login_2";
  private static final String PASSWORD_2 = "test_password_2";
  private static final String ROLE_USER = "USER";
  private static final String ROLE_ADMIN = "ADMIN";
  private static User user1;
  private static User user2;
  private static UserDto userDto1;
  private static UserDto userDto2;
  @Mock private UserRepository repository;
  @Mock private UserMapper mapper;
  @InjectMocks private UserServiceImpl service;

  @BeforeAll
  static void beforeAll() {

    Role role1 = new Role();
    role1.setId(1L);
    role1.setName(ROLE_USER);

    Role role2 = new Role();
    role2.setId(2L);
    role2.setName(ROLE_ADMIN);

    Set<Role> ROLES_1 = Set.of(role1, role2);
    Set<Role> ROLES_2 = Set.of(role1);

    user1 = new User();
    user1.setId(ID_1);
    user1.setLogin(LOGIN_1);
    user1.setPassword(PASSWORD_1);
    user1.setRoles(ROLES_1);

    user2 = new User();
    user2.setId(ID_2);
    user2.setLogin(LOGIN_2);
    user2.setPassword(PASSWORD_2);
    user2.setRoles(ROLES_2);

    userDto1 = new UserDto();
    userDto1.setId(ID_1);
    userDto1.setLogin(LOGIN_1);

    userDto2 = new UserDto();
    userDto2.setId(ID_2);
    userDto2.setLogin(LOGIN_2);
  }

  @Test
  public void testGetAllShouldRequestAllEntities() {
    // given
    List<User> expectedUserList = List.of(user1, user2);
    when(repository.findAll()).thenReturn(expectedUserList);
    when(mapper.mapToDto(user1)).thenReturn(userDto1);
    when(mapper.mapToDto(user2)).thenReturn(userDto2);
    // when
    List<UserDto> result = service.getAll();
    // then
    assertEquals(expectedUserList.size(), result.size());
    verify(repository).findAll();
    verify(mapper).mapToDto(user1);
    verify(mapper).mapToDto(user2);
  }

  @Test
  public void testGetByIdShouldRequestAOptionalWithDtoIfEntityWithProvidedIdExistsInTheDb() {
    // given
    when(repository.findById(ID_1)).thenReturn(Optional.of(user1));
    when(mapper.mapToDto(user1)).thenReturn(userDto1);
    // when
    Optional<UserDto> resultOpt = service.getById(ID_1);
    // then
    assertTrue(resultOpt.isPresent());
    resultOpt.ifPresent(
        result -> {
          assertEquals(user1.getId(), result.getId());
          assertEquals(user1.getLogin(), result.getLogin());
        });
    verify(repository).findById(ID_1);
    verify(mapper).mapToDto(user1);
  }
}
