package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignApplicationDto;
import io.realmarket.propeler.model.CampaignApplication;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.CampaignApplicationRepository;
import io.realmarket.propeler.service.CampaignApplicationService;
import io.realmarket.propeler.service.RequestStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CampaignApplicationServiceImpl implements CampaignApplicationService {

  private final CampaignApplicationRepository campaignApplicationRepository;
  private final RequestStateService requestStateService;

  @Autowired
  public CampaignApplicationServiceImpl(
      CampaignApplicationRepository campaignApplicationRepository,
      RequestStateService requestStateService) {
    this.campaignApplicationRepository = campaignApplicationRepository;
    this.requestStateService = requestStateService;
  }

  @Override
  public CampaignApplication applyForCampaign(CampaignApplicationDto campaignApplicationDto) {
    CampaignApplication campaignApplication = new CampaignApplication(campaignApplicationDto);
    campaignApplication.setRequestState(
        requestStateService.getRequestState(RequestStateName.PENDING));
    return campaignApplicationRepository.save(campaignApplication);
  }
}
