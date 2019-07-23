package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.CampaignDocumentsAccessRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignDocumentsAccessRequestRepository
    extends JpaRepository<CampaignDocumentsAccessRequest, Long> {}
