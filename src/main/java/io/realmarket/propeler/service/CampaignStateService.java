package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.enums.CampaignStateName;

public interface CampaignStateService {

  boolean ifStateCanBeChanged(
      CampaignState currentCampaignState, CampaignState followingCampaignState);

  void changeStateOrThrow(Campaign campaign, CampaignState followingCampaignState);

  CampaignState getCampaignState(String name);

  CampaignState getCampaignState(CampaignStateName campaignStateName);
}
