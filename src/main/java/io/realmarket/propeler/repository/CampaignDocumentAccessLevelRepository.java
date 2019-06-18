package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.DocumentAccessLevel;
import io.realmarket.propeler.model.enums.EDocumentAccessLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampaignDocumentAccessLevelRepository
    extends JpaRepository<DocumentAccessLevel, Long> {
  Optional<DocumentAccessLevel> findByName(EDocumentAccessLevel name);
}
