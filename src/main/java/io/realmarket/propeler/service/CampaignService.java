package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.CampaignDto;
import io.realmarket.propeler.api.dto.CampaignPatchDto;
import io.realmarket.propeler.model.Campaign;

public interface CampaignService {
  Campaign findByUrlFriendlyNameOrThrowException(String name);

  void createCampaign(CampaignDto campaignDto);

  CampaignDto patchCampaign(String campaignName, CampaignPatchDto campaignPatchDto);
}
