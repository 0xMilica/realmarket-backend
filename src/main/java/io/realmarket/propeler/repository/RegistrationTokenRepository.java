package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.RegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RegistrationTokenRepository extends JpaRepository<RegistrationToken, Long> {

  Optional<RegistrationToken> findByValueAndExpirationTimeGreaterThanEqual(
      String value, Instant date);

  void deleteAllByExpirationTimeLessThan(Instant date);
}
