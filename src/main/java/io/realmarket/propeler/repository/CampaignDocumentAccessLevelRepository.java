package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.CampaignDocumentAccessLevel;
import io.realmarket.propeler.model.enums.ECampaignDocumentAccessLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampaignDocumentAccessLevelRepository
    extends JpaRepository<CampaignDocumentAccessLevel, Long> {
  Optional<CampaignDocumentAccessLevel> findByName(ECampaignDocumentAccessLevel name);
}
