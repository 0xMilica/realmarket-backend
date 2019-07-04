package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.AuditRequestDto;
import io.realmarket.propeler.model.Audit;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.AuditRepository;
import io.realmarket.propeler.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditServiceImpl implements AuditService {

  private final AuditRepository auditRepository;
  private final AuthService authService;
  private final RequestStateService requestStateService;
  private final CampaignService campaignService;
  private final CampaignStateService campaignStateService;

  @Autowired
  public AuditServiceImpl(
      AuditRepository auditRepository,
      RequestStateService requestStateService,
      AuthService authService,
      CampaignService campaignService,
      CampaignStateService campaignStateService) {
    this.auditRepository = auditRepository;
    this.requestStateService = requestStateService;
    this.authService = authService;
    this.campaignService = campaignService;
    this.campaignStateService = campaignStateService;
  }

  @Override
  public Audit assignAuditRequest(AuditRequestDto auditRequestDto) {
    Auth auditorAuth = authService.findByIdOrThrowException(auditRequestDto.getAuditorId());
    Campaign campaign =
        campaignService.getCampaignByUrlFriendlyName(auditRequestDto.getCampaignUrlFriendlyName());
    campaignStateService.changeState(
        campaign, campaignStateService.getCampaignState(CampaignStateName.AUDIT));

    Audit audit =
        Audit.builder()
            .auditorAuth(auditorAuth)
            .campaign(campaign)
            .requestState(requestStateService.getRequestState(RequestStateName.PENDING))
            .build();

    return auditRepository.save(audit);
  }
}
