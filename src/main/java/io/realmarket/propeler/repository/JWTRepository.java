package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.JWT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface JWTRepository extends JpaRepository<JWT, Long> {
  Optional<JWT> findByValueAndExpirationTimeGreaterThanEqual(String value, Instant date);

  void deleteAllByExpirationTimeLessThan(Instant date);
}
