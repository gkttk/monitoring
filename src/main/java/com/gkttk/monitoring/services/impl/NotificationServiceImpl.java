package com.gkttk.monitoring.services.impl;

import com.gkttk.monitoring.components.mappers.NotificationMapper;
import com.gkttk.monitoring.dao.repositories.NotificationRepository;
import com.gkttk.monitoring.dao.specifications.NotificationEqualSpecification;
import com.gkttk.monitoring.models.dtos.NotificationDto;
import com.gkttk.monitoring.models.dtos.NotificationFilter;
import com.gkttk.monitoring.models.entities.Notification;
import com.gkttk.monitoring.services.NotificationService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository repository;
  private final NotificationMapper mapper;

  @Override
  public Optional<NotificationDto> getById(Long id) {

    Optional<Notification> entityOpt = repository.findById(id);

    if (entityOpt.isPresent()) {
      Notification entity = entityOpt.get();
      NotificationDto dto = mapper.mapToDto(entity);
      return Optional.of(dto);
    } else {
      return Optional.empty();
    }
  }

  @Override
  @SuppressWarnings("ConstantConditions")
  public Long getAllCount(NotificationFilter filter) {

    NotificationEqualSpecification specification =
        filter == null ? null : new NotificationEqualSpecification(filter);
    return repository.count(specification);
  }

  @Override
  @SuppressWarnings("ConstantConditions")
  public List<NotificationDto> getAll(NotificationFilter filter) {

    NotificationEqualSpecification specification =
        filter == null ? null : new NotificationEqualSpecification(filter);
    List<Notification> entities = repository.findAll(specification);
    return entities.stream().map(mapper::mapToDto).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public NotificationDto create(NotificationDto dto) {

    Notification entity = mapper.mapToEntity(dto);
    entity.setTimestamp(LocalDateTime.now());
    Notification savedEntity = repository.save(entity);

    return mapper.mapToDto(savedEntity);
  }

  @Override
  @Transactional
  public void deleteById(Long id) {

    repository.deleteById(id);
  }
}
