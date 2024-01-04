package com.gkttk.monitoring.dao.repositories;

import com.gkttk.monitoring.models.entities.SystemComponent;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemComponentRepository extends CrudRepository<SystemComponent, Long> {

  Optional<SystemComponent> findByName(String name);
}
