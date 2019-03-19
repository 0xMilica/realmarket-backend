package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.RememberMeCookie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RememberMeCookieRepository extends JpaRepository<RememberMeCookie, Long> {

  Optional<RememberMeCookie> findByValueAndAuthAndExpirationTimeGreaterThanEqual(
      String value, Auth auth, Instant date);

  void deleteAllByExpirationTimeLessThan(Instant date);
}
