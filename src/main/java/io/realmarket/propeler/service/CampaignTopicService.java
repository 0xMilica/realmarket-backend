package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.CampaignTopicDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignTopic;
import io.realmarket.propeler.model.CampaignTopicType;

public interface CampaignTopicService {
  void createCampaignTopic(
      String campaignName, String topicType, CampaignTopicDto campaignTopicDto);

  CampaignTopicDto getCampaignTopic(String campaignName, String topicType);

  CampaignTopicType findByTopicTypeOrThrowException(String topicType);

  CampaignTopic findByCampaignAndCampaignTopicTypeOrThrowException(
      Campaign campaign, CampaignTopicType campaignTopicType);

  void updateCampaignTopic(
      String campaignName, String topicType, CampaignTopicDto campaignTopicDto);
}
