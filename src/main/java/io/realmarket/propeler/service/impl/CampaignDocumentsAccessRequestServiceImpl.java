package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignDocumentsAccessRequest;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.CampaignDocumentsAccessRequestRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignDocumentsAccessRequestService;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.RequestStateService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CampaignDocumentsAccessRequestServiceImpl
    implements CampaignDocumentsAccessRequestService {

  private final CampaignDocumentsAccessRequestRepository campaignDocumentsAccessRequestRepository;
  private final CampaignService campaignService;
  private final RequestStateService requestStateService;

  @Autowired
  public CampaignDocumentsAccessRequestServiceImpl(
      CampaignDocumentsAccessRequestRepository campaignDocumentsAccessRequestRepository,
      CampaignService campaignService,
      RequestStateService requestStateService) {
    this.campaignDocumentsAccessRequestRepository = campaignDocumentsAccessRequestRepository;
    this.campaignService = campaignService;
    this.requestStateService = requestStateService;
  }

  @Override
  public CampaignDocumentsAccessRequest sendCampaignDocumentsAccessRequest(String campaignName) {
    Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    if (!campaign.getCampaignState().getName().equals(CampaignStateName.ACTIVE)) {
      throw new BadRequestException(ExceptionMessages.INVALID_REQUEST);
    }
    Auth auth = AuthenticationUtil.getAuthentication().getAuth();

    return campaignDocumentsAccessRequestRepository.save(
        CampaignDocumentsAccessRequest.builder()
            .campaign(campaign)
            .auth(auth)
            .requestState(requestStateService.getRequestState(RequestStateName.PENDING))
            .build());
  }
}
