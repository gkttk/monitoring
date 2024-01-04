package com.gkttk.monitoring.components.mappers;

import com.gkttk.monitoring.models.dtos.SystemConfigurationDto;
import com.gkttk.monitoring.models.entities.SystemConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SystemConfigurationMapper {

  SystemConfigurationDto mapToDto(SystemConfiguration entity);

  SystemConfiguration mapToEntity(SystemConfigurationDto dto);
}
