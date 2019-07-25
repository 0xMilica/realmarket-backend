package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignDocumentsAccessRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignDocumentsAccessRequestRepository
    extends JpaRepository<CampaignDocumentsAccessRequest, Long> {

  List<CampaignDocumentsAccessRequest> findByCampaign(Campaign campaign);
}
