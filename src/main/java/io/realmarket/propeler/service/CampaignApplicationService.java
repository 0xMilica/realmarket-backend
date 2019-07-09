package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.CampaignApplicationDto;
import io.realmarket.propeler.model.CampaignApplication;

public interface CampaignApplicationService {
  CampaignApplication applyForCampaign(CampaignApplicationDto campaignApplicationDto);
}
