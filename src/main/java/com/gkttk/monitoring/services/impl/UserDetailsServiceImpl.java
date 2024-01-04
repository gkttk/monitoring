package com.gkttk.monitoring.services.impl;

import com.gkttk.monitoring.dao.repositories.UserRepository;
import com.gkttk.monitoring.models.entities.User;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
        repository
            .findByLogin(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        String.format("User '%s' is not found", username)));

    return new org.springframework.security.core.userdetails.User(
        user.getLogin(),
        user.getPassword(),
        user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList()));
  }
}
