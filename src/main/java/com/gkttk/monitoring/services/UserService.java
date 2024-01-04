package com.gkttk.monitoring.services;

import com.gkttk.monitoring.models.dtos.UserDto;
import java.util.List;
import java.util.Optional;

public interface UserService {

  List<UserDto> getAll();

  Optional<UserDto> getById(Long id);
}
