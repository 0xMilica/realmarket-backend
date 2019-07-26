package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignDocumentsAccessRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignDocumentsAccessRequestRepository
    extends JpaRepository<CampaignDocumentsAccessRequest, Long> {

  CampaignDocumentsAccessRequest findByCampaignAndAuth(Campaign campaign, Auth auth);

  List<CampaignDocumentsAccessRequest> findByCampaign(Campaign campaign);
}
