package com.gkttk.monitoring.services;

import com.gkttk.monitoring.models.dtos.NotificationDto;
import com.gkttk.monitoring.models.dtos.NotificationFilter;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface NotificationService {

  Optional<NotificationDto> getById(Long id);

  List<NotificationDto> getAll(@Nullable NotificationFilter filter);

  Long getAllCount(NotificationFilter filter);

  NotificationDto create(NotificationDto dto);

  void deleteById(Long id);
}
