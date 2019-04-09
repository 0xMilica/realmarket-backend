package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.CampaignTopic;
import io.realmarket.propeler.model.CampaignTopicImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignTopicImageRepository extends JpaRepository<CampaignTopicImage, Long> {
  List<CampaignTopicImage> findByCampaignTopic(CampaignTopic campaignTopic);
}
