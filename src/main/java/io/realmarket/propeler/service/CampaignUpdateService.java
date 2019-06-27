package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.CampaignUpdateDto;
import io.realmarket.propeler.api.dto.CampaignUpdateResponseDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignUpdate;
import io.realmarket.propeler.model.enums.CampaignStateName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CampaignUpdateService {

  CampaignUpdate findByIdOrThrowException(Long id);

  Page<CampaignUpdate> findCampaignUpdates(Pageable pageable);

  Page<CampaignUpdate> findMyCampaignUpdates(Auth auth, Pageable pageable);

  Page<CampaignUpdate> findCampaignUpdatesByCampaignState(
      CampaignStateName campaignState, Pageable pageable);

  Page<CampaignUpdate> findCampaignUpdatesByCampaign(Campaign campaign, Pageable pageable);

  CampaignUpdateResponseDto createCampaignUpdate(
      String campaignName, CampaignUpdateDto campaignUpdateDto);

  CampaignUpdateResponseDto updateCampaignUpdate(Long id, CampaignUpdateDto campaignUpdateDto);

  CampaignUpdateResponseDto getCampaignUpdate(Long id);

  Page<CampaignUpdateResponseDto> getCampaignUpdates(Pageable pageable, String filter);

  Page<CampaignUpdateResponseDto> getCampaignUpdatesForCampaign(
      String campaignName, Pageable pageable);
}
