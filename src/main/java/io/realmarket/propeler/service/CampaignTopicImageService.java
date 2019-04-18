package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.FilenameDto;
import io.realmarket.propeler.model.CampaignTopic;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface CampaignTopicImageService {
  void removeObsoleteImages(CampaignTopic campaignTopic);

  FilenameDto uploadImage(
      HttpServletRequest request, String campaignName, String topicType, MultipartFile image);
}
