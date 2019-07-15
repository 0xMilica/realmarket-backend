package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.enums.CampaignStateName;

public interface CampaignStateService {

  boolean ifStateCanBeChanged(
      CampaignStateName currentCampaignState, CampaignStateName followingCampaignState);

  void changeStateOrThrow(Campaign campaign, CampaignStateName followingCampaignState);

  CampaignState getCampaignState(String name);

  CampaignState getCampaignState(CampaignStateName campaignStateName);
}
