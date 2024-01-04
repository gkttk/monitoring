package com.gkttk.monitoring.components.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gkttk.monitoring.models.dtos.UserDto;
import com.gkttk.monitoring.models.entities.Role;
import com.gkttk.monitoring.models.entities.User;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class UserMapperTest {

  private static final Long ID = 1L;
  private static final String LOGIN = "test_login";
  private static final String PASSWORD = "test_password";
  private static Set<Role> ROLES;
  private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

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
  public void testMapperTurnEntityIntoDto() {
    // given
    User user = new User();
    user.setId(ID);
    user.setLogin(LOGIN);
    user.setPassword(PASSWORD);
    user.setRoles(ROLES);
    // when
    UserDto result = mapper.mapToDto(user);
    // then
    assertEquals(user.getId(), result.getId());
    assertEquals(user.getLogin(), result.getLogin());
  }

  @Test
  public void testMapperTurnDtoIntoEntity() {
    // given
    UserDto dto = new UserDto();
    dto.setId(ID);
    dto.setLogin(LOGIN);
    // when
    User result = mapper.mapToEntity(dto);
    // then
    assertEquals(dto.getId(), result.getId());
    assertEquals(dto.getLogin(), result.getLogin());
  }
}
