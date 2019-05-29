package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;
import io.realmarket.propeler.model.enums.CampaignStateName;

public interface CampaignStateService {

  boolean changeState(Campaign campaign, CampaignState followingCampaignState, boolean isOwner);

  boolean hasReadAccess();

  boolean hasWriteAccess();

  CampaignState getCampaignState(String name);

  CampaignState getCampaignState(CampaignStateName campaignStateName);
}
