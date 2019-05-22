package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignState;

public interface CampaignStateService {

    void changeState(String campaignUrlFriendlyName, CampaignState followingCampaignState);

    boolean hasReadAccess();

    boolean hasWriteAccess();
}
