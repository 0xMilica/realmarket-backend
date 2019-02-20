package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.AuthorizedAction;
import io.realmarket.propeler.model.enums.EAuthorizationActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface AuthorizedActionRepository extends JpaRepository<AuthorizedAction, Long> {

  Optional<AuthorizedAction> findByAuthAndType(Auth auth, EAuthorizationActionType type);

  void deleteAllByAuthAndType(Auth auth, EAuthorizationActionType type);

  Optional<AuthorizedAction> findByAuthAndTypeAndExpirationIsAfter(
      Auth auth, EAuthorizationActionType type, Instant now);
}
