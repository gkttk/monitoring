package com.gkttk.monitoring.services;

import com.gkttk.monitoring.models.dtos.SystemConfigurationDto;
import com.gkttk.monitoring.models.entities.SystemConfiguration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SystemConfigurationService {

  Optional<String> getValueByKey(String key);

  Map<String, SystemConfiguration> getByKeys(List<String> keys);

  List<SystemConfigurationDto> getAll();

  SystemConfigurationDto updateValueById(Long id, String newValue);
}
