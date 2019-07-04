package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.enums.CampaignStateName;

public interface CampaignStateService {

  void changeState(Campaign campaign, CampaignState followingCampaignState);

  CampaignState getCampaignState(String name);

  CampaignState getCampaignState(CampaignStateName campaignStateName);
}
