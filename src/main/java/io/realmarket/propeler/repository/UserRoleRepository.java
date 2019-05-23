package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.UserRole;
import io.realmarket.propeler.model.enums.EUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
  Optional<UserRole> findByName(EUserRole name);
}
