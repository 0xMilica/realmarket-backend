package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignTopic;
import io.realmarket.propeler.model.CampaignTopicType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignTopicRepository extends JpaRepository<CampaignTopic, Long> {
  Optional<CampaignTopic> findByCampaignAndCampaignTopicType(
      Campaign campaign, CampaignTopicType campaignTopicType);

  @Query("Select ct.campaignTopicType FROM CampaignTopic ct WHERE ct.campaign = ?1")
  List<CampaignTopicType> selectAllTopicsByCampaign(Campaign campaign);
}
