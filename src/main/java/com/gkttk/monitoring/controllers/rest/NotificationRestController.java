package com.gkttk.monitoring.controllers.rest;

import com.gkttk.monitoring.models.dtos.NotificationDto;
import com.gkttk.monitoring.models.dtos.NotificationFilter;
import com.gkttk.monitoring.services.NotificationService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationRestController {

  private final NotificationService service;

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<NotificationDto> getNotification(@PathVariable Long id) {

    Optional<NotificationDto> resultOpt = service.getById(id);

    return resultOpt.isPresent() ? ResponseEntity.of(resultOpt) : ResponseEntity.notFound().build();
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public List<NotificationDto> getAllNotifications(NotificationFilter filter) {

    return service.getAll(filter);
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public NotificationDto createNotification(@RequestBody NotificationDto dto) {

    return service.create(dto);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public void deleteNotification(@PathVariable Long id) {
    service.deleteById(id);
  }
}
