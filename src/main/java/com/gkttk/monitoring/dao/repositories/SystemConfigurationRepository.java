package com.gkttk.monitoring.dao.repositories;

import com.gkttk.monitoring.models.entities.SystemConfiguration;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConfigurationRepository extends CrudRepository<SystemConfiguration, Long> {

  Optional<SystemConfiguration> findByKey(String key);

  List<SystemConfiguration> findByKeyIn(List<String> keys);
}
