package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.FilenameDto;
import io.realmarket.propeler.model.CampaignTopic;
import org.springframework.web.multipart.MultipartFile;

public interface CampaignTopicImageService {
  void removeObsoleteImages(CampaignTopic campaignTopic);

  FilenameDto uploadImage(String campaignName, String topicType, MultipartFile image);
}
