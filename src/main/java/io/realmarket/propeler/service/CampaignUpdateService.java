package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.CampaignUpdateDto;
import io.realmarket.propeler.api.dto.CampaignUpdateResponseDto;
import io.realmarket.propeler.model.CampaignUpdate;

public interface CampaignUpdateService {

  CampaignUpdate findByIdOrThrowException(Long id);

  CampaignUpdateResponseDto createCampaignUpdate(
      String campaignName, CampaignUpdateDto campaignUpdateDto);

  CampaignUpdateResponseDto updateCampaignUpdate(Long id, CampaignUpdateDto campaignUpdateDto);

  CampaignUpdateResponseDto getCampaignUpdate(Long id);
}
