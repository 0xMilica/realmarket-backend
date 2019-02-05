package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.TemporaryToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TemporaryTokenRepository extends JpaRepository<TemporaryToken, Long> {

  Optional<TemporaryToken> findByValueAndExpirationTimeGreaterThanEqual(String value, Date date);

  void deleteAllByExpirationTimeLessThan(Date date);
}
