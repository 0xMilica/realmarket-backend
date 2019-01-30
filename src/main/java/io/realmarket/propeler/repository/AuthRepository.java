package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
  Optional<Auth> findByUsername(String username);

  void deleteByRegistrationTokenExpirationTimeLessThanAndActiveIsFalse(Date date);
}
