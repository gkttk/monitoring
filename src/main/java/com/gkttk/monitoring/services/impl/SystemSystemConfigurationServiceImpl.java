package com.gkttk.monitoring.services.impl;

import com.gkttk.monitoring.components.mappers.SystemConfigurationMapper;
import com.gkttk.monitoring.dao.repositories.SystemConfigurationRepository;
import com.gkttk.monitoring.exceptions.EntityNotFound;
import com.gkttk.monitoring.models.dtos.SystemConfigurationDto;
import com.gkttk.monitoring.models.entities.SystemConfiguration;
import com.gkttk.monitoring.services.SystemConfigurationService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SystemSystemConfigurationServiceImpl implements SystemConfigurationService {

  private final SystemConfigurationRepository repository;
  private final SystemConfigurationMapper mapper;

  @Override
  public Map<String, SystemConfiguration> getByKeys(List<String> keys) {

    List<SystemConfiguration> configs = repository.findByKeyIn(keys);
    return configs.stream()
        .collect(Collectors.toMap(SystemConfiguration::getKey, Function.identity()));
  }

  @Override
  public List<SystemConfigurationDto> getAll() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .map(mapper::mapToDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public SystemConfigurationDto updateValueById(Long id, String newValue) {

    SystemConfiguration configuration =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFound(
                        String.format("Configuration with provided id %s is not found.", id)));

    configuration.setValue(newValue);
    SystemConfiguration updatedEntity = repository.save(configuration);

    return mapper.mapToDto(updatedEntity);
  }

  @Override
  public Optional<String> getValueByKey(String key) {

    Optional<SystemConfiguration> configOpt = repository.findByKey(key);

    if (configOpt.isPresent()) {
      SystemConfiguration configuration = configOpt.get();
      return Optional.of(configuration.getValue());
    } else {
      return Optional.empty();
    }
  }
}
