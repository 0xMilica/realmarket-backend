package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.CampaignDto;
import io.realmarket.propeler.api.dto.CampaignPatchDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.model.Campaign;
import org.springframework.web.multipart.MultipartFile;

public interface CampaignService {
  Campaign findByUrlFriendlyNameOrThrowException(String name);

  void createCampaign(CampaignDto campaignDto);

  CampaignDto patchCampaign(String campaignName, CampaignPatchDto campaignPatchDto);

  void uploadMarketImage(String campaignName, MultipartFile logo);

  FileDto downloadMarketImage(String campaignName);

  void deleteMarketImage(String campaignName);
}
