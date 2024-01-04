package com.gkttk.monitoring.controllers.graphql;

import com.gkttk.monitoring.models.dtos.SystemConfigurationDto;
import com.gkttk.monitoring.services.SystemConfigurationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConfigurationGraphQLController {

  private final SystemConfigurationService service;

  @QueryMapping
  @PreAuthorize("hasRole('ADMIN')")
  public List<SystemConfigurationDto> configurations() {
    return service.getAll();
  }

  @MutationMapping
  @PreAuthorize("hasRole('ADMIN')")
  public SystemConfigurationDto updateConfiguration(@Argument Long id, @Argument String newValue) {
    return service.updateValueById(id, newValue);
  }
}
