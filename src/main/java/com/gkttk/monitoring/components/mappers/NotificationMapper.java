package com.gkttk.monitoring.components.mappers;

import com.gkttk.monitoring.dao.repositories.SystemComponentRepository;
import com.gkttk.monitoring.exceptions.EntityNotFound;
import com.gkttk.monitoring.models.dtos.NotificationDto;
import com.gkttk.monitoring.models.entities.Notification;
import com.gkttk.monitoring.models.entities.SystemComponent;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class NotificationMapper {

  @Autowired private SystemComponentRepository componentRepository;

  @Mapping(target = "componentName", source = "component.name")
  public abstract NotificationDto mapToDto(Notification entity);

  @Mapping(source = "componentName", target = "component", qualifiedByName = "compNameToComp")
  public abstract Notification mapToEntity(NotificationDto dto);

  @Named("compNameToComp")
  public SystemComponent componentNameToComponent(String componentName) {
    return componentRepository.findByName(componentName).orElse(null);
  }

  @AfterMapping
  public void afterMapping(@MappingTarget Notification entity, NotificationDto dto) {
    if (entity.getComponent() == null) {
      throw new EntityNotFound(
          String.format(
              "Provided in DTO component name %s doesn't exist in the DB", dto.getComponentName()));
    }
  }
}
