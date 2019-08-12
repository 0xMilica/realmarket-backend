package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.UserRole;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.UserRoleRepository;
import io.realmarket.propeler.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class UserRoleServiceImpl implements UserRoleService {

  private final UserRoleRepository userRoleRepository;

  @Autowired
  public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
    this.userRoleRepository = userRoleRepository;
  }

  @Override
  public UserRole getUserRole(UserRoleName userRoleName) {
    return userRoleRepository.findByName(userRoleName).orElseThrow(EntityNotFoundException::new);
  }

  @Override
  public UserRole getUserRole(String name) {
    return userRoleRepository
        .findByName(UserRoleName.valueOf(name.toUpperCase()))
        .orElseThrow(EntityNotFoundException::new);
  }
}
