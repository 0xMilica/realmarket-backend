package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignDocumentRepository extends JpaRepository<CampaignDocument, Long> {

  List<CampaignDocument> findAllByCampaign(Campaign campaign);

  List<CampaignDocument> findAllByCampaignIn(List<Campaign> campaigns);

  Page<CampaignDocument> findAllByCampaignIn(List<Campaign> campaigns, Pageable pageable);

  List<CampaignDocument> findAllByCampaignOrderByUploadDateDesc(Campaign campaign);
}
