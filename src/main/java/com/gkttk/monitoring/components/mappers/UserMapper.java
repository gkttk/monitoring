package com.gkttk.monitoring.components.mappers;

import com.gkttk.monitoring.models.dtos.UserDto;
import com.gkttk.monitoring.models.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  UserDto mapToDto(User entity);

  User mapToEntity(UserDto dto);
}
