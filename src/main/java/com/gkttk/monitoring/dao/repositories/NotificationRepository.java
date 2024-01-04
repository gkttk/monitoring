package com.gkttk.monitoring.dao.repositories;

import com.gkttk.monitoring.models.entities.Notification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository
    extends CrudRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {}
