package com.gkttk.monitoring.dao.repositories;

import com.gkttk.monitoring.models.entities.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByLoginAndPassword(String login, String password);

  Optional<User> findByLogin(String login);
}
