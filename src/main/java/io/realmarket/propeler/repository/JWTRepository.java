package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.JWT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface JWTRepository extends JpaRepository<JWT, Long> {
  Optional<JWT> findByValueAndExpirationTimeGreaterThanEqual(String value, Date date);

  void deleteAllByExpirationTimeLessThan(Date date);
}
