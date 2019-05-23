package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.AuthorizedAction;
import io.realmarket.propeler.model.enums.EAuthorizedActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface AuthorizedActionRepository extends JpaRepository<AuthorizedAction, Long> {

  Optional<AuthorizedAction> findByAuthAndType(Auth auth, EAuthorizedActionType type);

  void deleteAllByAuthAndTypeName(Auth auth, EAuthorizedActionType type);

  Optional<AuthorizedAction> findByAuthAndTypeNameAndExpirationIsAfter(
      Auth auth, EAuthorizedActionType type, Instant now);
}
