package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.AuthorizedActionType;
import io.realmarket.propeler.model.enums.EAuthorizedActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizedActionTypeRepository extends JpaRepository<AuthorizedActionType, Long> {
  Optional<AuthorizedActionType> findByName(EAuthorizedActionType name);
}
