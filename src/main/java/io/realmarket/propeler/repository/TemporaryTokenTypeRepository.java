package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.TemporaryTokenType;
import io.realmarket.propeler.model.enums.ETemporaryTokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemporaryTokenTypeRepository extends JpaRepository<TemporaryTokenType, Long> {
  Optional<TemporaryTokenType> findByName(ETemporaryTokenType name);
}
