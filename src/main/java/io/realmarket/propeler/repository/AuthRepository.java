package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.enums.UserRoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
  Optional<Auth> findByUsername(String username);

  Optional<Auth> findByPersonId(Long personId);

  List<Auth> findAllByUserRoleName(UserRoleName userRoleName);
}
