package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.CampaignUpdate;
import io.realmarket.propeler.model.CampaignUpdateImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignUpdateImageRepository extends JpaRepository<CampaignUpdateImage, Long> {

  List<CampaignUpdateImage> findByCampaignUpdate(CampaignUpdate campaignUpdate);
}
