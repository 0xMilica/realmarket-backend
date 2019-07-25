package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignDocumentsAccessRequestDto;
import io.realmarket.propeler.api.dto.CampaignDocumentsAccessRequestsDto;
import io.realmarket.propeler.api.dto.CampaignResponseDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignDocumentsAccessRequest;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.CampaignDocumentsAccessRequestRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignDocumentsAccessRequestServiceImpl
    implements CampaignDocumentsAccessRequestService {

  private final CampaignDocumentsAccessRequestRepository campaignDocumentsAccessRequestRepository;
  private final CampaignService campaignService;
  private final AuthService authService;
  private final RequestStateService requestStateService;

  @Autowired
  public CampaignDocumentsAccessRequestServiceImpl(
      CampaignDocumentsAccessRequestRepository campaignDocumentsAccessRequestRepository,
      CompanyService companyService,
      CampaignService campaignService,
      AuthService authService,
      RequestStateService requestStateService) {
    this.campaignDocumentsAccessRequestRepository = campaignDocumentsAccessRequestRepository;
    this.campaignService = campaignService;
    this.authService = authService;
    this.requestStateService = requestStateService;
  }

  private CampaignDocumentsAccessRequest findByIdOrThrowException(Long requestId) {
    return campaignDocumentsAccessRequestRepository
        .findById(requestId)
        .orElseThrow(EntityNotFoundException::new);
  }

  private List<CampaignDocumentsAccessRequest> findByCampaign(Campaign campaign) {
    return campaignDocumentsAccessRequestRepository.findByCampaign(campaign);
  }

  @Override
  public CampaignDocumentsAccessRequest sendCampaignDocumentsAccessRequest(String campaignName) {
    Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    if (!campaign.getCampaignState().getName().equals(CampaignStateName.ACTIVE)) {
      throw new BadRequestException(ExceptionMessages.INVALID_REQUEST);
    }
    Auth auth = AuthenticationUtil.getAuthentication().getAuth();
    auth = authService.findById(auth.getId()).get();

    return campaignDocumentsAccessRequestRepository.save(
        CampaignDocumentsAccessRequest.builder()
            .campaign(campaign)
            .auth(auth)
            .requestState(requestStateService.getRequestState(RequestStateName.PENDING))
            .build());
  }

  @Override
  public CampaignDocumentsAccessRequestsDto getCampaignDocumentsAccessRequests() {
    Campaign activeCampaign = campaignService.getActiveCampaign();

    return getRequestsForCampaign(activeCampaign);
  }

  private CampaignDocumentsAccessRequestsDto getRequestsForCampaign(Campaign campaign) {
    CampaignDocumentsAccessRequestsDto campaignDocumentsAccessRequestsDto =
        new CampaignDocumentsAccessRequestsDto();
    campaignDocumentsAccessRequestsDto.setCampaign(new CampaignResponseDto(campaign));

    List<CampaignDocumentsAccessRequestDto> requestList =
        findByCampaign(campaign).stream()
            .map(CampaignDocumentsAccessRequestDto::new)
            .collect(Collectors.toList());
    campaignDocumentsAccessRequestsDto.setRequests(requestList);

    return campaignDocumentsAccessRequestsDto;
  }

  @Override
  public CampaignDocumentsAccessRequest acceptCampaignDocumentsAccessRequest(Long requestId) {
    CampaignDocumentsAccessRequest campaignDocumentsAccessRequest =
        findByIdOrThrowException(requestId);
    campaignService.throwIfNoAccess(campaignDocumentsAccessRequest.getCampaign());
    campaignDocumentsAccessRequest.setRequestState(
        requestStateService.getRequestState(RequestStateName.APPROVED));

    return campaignDocumentsAccessRequestRepository.save(campaignDocumentsAccessRequest);
  }

  @Override
  public CampaignDocumentsAccessRequest rejectCampaignDocumentsAccessRequest(Long requestId) {
    CampaignDocumentsAccessRequest campaignDocumentsAccessRequest =
        findByIdOrThrowException(requestId);
    campaignService.throwIfNoAccess(campaignDocumentsAccessRequest.getCampaign());
    campaignDocumentsAccessRequest.setRequestState(
        requestStateService.getRequestState(RequestStateName.DECLINED));

    return campaignDocumentsAccessRequestRepository.save(campaignDocumentsAccessRequest);
  }
}
