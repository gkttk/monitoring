package com.gkttk.monitoring.controllers.graphql;

import com.gkttk.monitoring.models.dtos.UserDto;
import com.gkttk.monitoring.services.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserGraphQLController {

  private final UserService service;

  @QueryMapping
  @PreAuthorize("hasRole('ADMIN')")
  public UserDto userById(@Argument Long id) {
    return service.getById(id).orElse(null);
  }

  @QueryMapping
  @PreAuthorize("hasRole('ADMIN')")
  public List<UserDto> users() {
    return service.getAll();
  }
}
