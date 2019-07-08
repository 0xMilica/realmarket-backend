package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.AuditRequestDto;
import io.realmarket.propeler.model.Audit;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.AuditRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.AUDITING_REQUEST_NOT_FOUND;
import static io.realmarket.propeler.service.exception.util.ExceptionMessages.NOT_CAMPAIGN_AUDITOR;

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
  public Audit assignAudit(AuditRequestDto auditRequestDto) {
    Auth auditorAuth = authService.findByIdOrThrowException(auditRequestDto.getAuditorId());
    Campaign campaign =
        campaignService.getCampaignByUrlFriendlyName(auditRequestDto.getCampaignUrlFriendlyName());
    campaignService.changeCampaignStateOrThrow(
        campaign, campaignStateService.getCampaignState(CampaignStateName.AUDIT));

    Audit audit =
        Audit.builder()
            .auditorAuth(auditorAuth)
            .campaign(campaign)
            .requestState(requestStateService.getRequestState(RequestStateName.PENDING))
            .build();

    return auditRepository.save(audit);
  }

  @Override
  public Audit acceptCampaign(Long auditId) {
    Audit audit = findByIdOrThrowException(auditId);
    throwIfNoAccess(audit);

    campaignService.changeCampaignStateOrThrow(
        audit.getCampaign(), campaignStateService.getCampaignState(CampaignStateName.LAUNCH_READY));
    audit.setRequestState(requestStateService.getRequestState(RequestStateName.APPROVED));
    return auditRepository.save(audit);
  }

  @Override
  public Audit declineCampaign(Long auditId, String content) {
    Audit audit = findByIdOrThrowException(auditId);
    throwIfNoAccess(audit);

    campaignService.changeCampaignStateOrThrow(
        audit.getCampaign(), campaignStateService.getCampaignState(CampaignStateName.INITIAL));
    audit.setContent(content);
    audit.setRequestState(requestStateService.getRequestState(RequestStateName.DECLINED));
    return auditRepository.save(audit);
  }

  private Audit findByIdOrThrowException(Long auditId) {
    return auditRepository
        .findById(auditId)
        .orElseThrow(() -> new EntityNotFoundException(AUDITING_REQUEST_NOT_FOUND));
  }

  private void throwIfNoAccess(Audit audit) {
    if (!isAuditor(audit)) {
      throw new ForbiddenOperationException(NOT_CAMPAIGN_AUDITOR);
    }
  }

  private boolean isAuditor(Audit audit) {
    return audit.getAuditorAuth().getId().equals(AuthenticationUtil.getAuthentication().getAuth().getId());
  }
}
