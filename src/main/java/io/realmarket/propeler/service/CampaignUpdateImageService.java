package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.FilenameDto;
import io.realmarket.propeler.model.CampaignUpdate;
import org.springframework.web.multipart.MultipartFile;

public interface CampaignUpdateImageService {

  void removeObsoleteImages(CampaignUpdate campaignUpdate);

  FilenameDto uploadImage(Long campaignUpdateId, MultipartFile image);

  void removeImages(CampaignUpdate campaignUpdate);
}
