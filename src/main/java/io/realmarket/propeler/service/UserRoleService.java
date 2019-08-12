package io.realmarket.propeler.service;

import io.realmarket.propeler.model.UserRole;
import io.realmarket.propeler.model.enums.UserRoleName;

public interface UserRoleService {
  UserRole getUserRole(UserRoleName userRoleName);

  UserRole getUserRole(String name);
}
