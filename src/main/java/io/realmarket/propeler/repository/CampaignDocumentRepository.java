package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.CampaignDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignDocumentRepository extends JpaRepository<CampaignDocument, Long> {}
