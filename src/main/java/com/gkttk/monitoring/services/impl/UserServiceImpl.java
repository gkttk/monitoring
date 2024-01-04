package com.gkttk.monitoring.services.impl;

import com.gkttk.monitoring.components.mappers.UserMapper;
import com.gkttk.monitoring.dao.repositories.UserRepository;
import com.gkttk.monitoring.models.dtos.UserDto;
import com.gkttk.monitoring.models.entities.User;
import com.gkttk.monitoring.services.UserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

  private final UserRepository repository;
  private final UserMapper mapper;

  @Override
  public List<UserDto> getAll() {
    Iterable<User> entities = repository.findAll();
    return StreamSupport.stream(entities.spliterator(), false)
        .map(mapper::mapToDto)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<UserDto> getById(Long id) {

    Optional<User> entity = repository.findById(id);

    return entity.map(mapper::mapToDto);
  }
}
