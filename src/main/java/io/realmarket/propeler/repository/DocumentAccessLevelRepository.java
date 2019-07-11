package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.DocumentAccessLevel;
import io.realmarket.propeler.model.enums.DocumentAccessLevelName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentAccessLevelRepository extends JpaRepository<DocumentAccessLevel, Long> {
  Optional<DocumentAccessLevel> findByName(DocumentAccessLevelName name);
}
