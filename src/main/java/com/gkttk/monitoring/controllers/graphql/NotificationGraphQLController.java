package com.gkttk.monitoring.controllers.graphql;

import com.gkttk.monitoring.models.dtos.NotificationDto;
import com.gkttk.monitoring.models.dtos.NotificationFilter;
import com.gkttk.monitoring.services.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationGraphQLController {

  private final NotificationService service;

  @QueryMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public List<NotificationDto> notifications(@Argument(name = "filter") NotificationFilter filter) {
    return service.getAll(filter);
  }

  @MutationMapping
  @PreAuthorize("hasRole('ADMIN')")
  public NotificationDto createNotification(@Argument NotificationDto notification) {
    return service.create(notification);
  }
}
