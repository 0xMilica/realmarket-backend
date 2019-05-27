package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;

public interface CampaignStateService {

  boolean changeState(Campaign campaign, CampaignState followingCampaignState, boolean isOwner);

  boolean hasReadAccess();

  boolean hasWriteAccess();
}
