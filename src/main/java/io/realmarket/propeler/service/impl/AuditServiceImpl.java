package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.AuditRequestDto;
import io.realmarket.propeler.model.Audit;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.AuditRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.blockchain.BlockchainCommunicationService;
import io.realmarket.propeler.service.blockchain.BlockchainMethod;
import io.realmarket.propeler.service.blockchain.dto.campaign.ChangeStateDto;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.*;

@Service
public class AuditServiceImpl implements AuditService {

  private final AuditRepository auditRepository;
  private final AuthService authService;
  private final RequestStateService requestStateService;
  private final CampaignService campaignService;
  private final CampaignStateService campaignStateService;
  private final BlockchainCommunicationService blockchainCommunicationService;

  @Autowired
  public AuditServiceImpl(
      AuditRepository auditRepository,
      RequestStateService requestStateService,
      AuthService authService,
      CampaignService campaignService,
      CampaignStateService campaignStateService,
      BlockchainCommunicationService blockchainCommunicationService) {
    this.auditRepository = auditRepository;
    this.requestStateService = requestStateService;
    this.authService = authService;
    this.campaignService = campaignService;
    this.campaignStateService = campaignStateService;
    this.blockchainCommunicationService = blockchainCommunicationService;
  }

  @Override
  public Audit assignAudit(AuditRequestDto auditRequestDto) {
    Auth auditorAuth = authService.findByIdOrThrowException(auditRequestDto.getAuditorId());
    // TODO: Change this condition when assigning become possible for other roles too
    if (!auditorAuth.getUserRole().getName().equals(UserRoleName.ROLE_ADMIN)) {
      throw new BadRequestException(USER_CAN_NOT_BE_AUDITOR);
    }
    Campaign campaign =
        campaignService.getCampaignByUrlFriendlyName(auditRequestDto.getCampaignUrlFriendlyName());
    campaignService.changeCampaignStateOrThrow(
        campaign, campaignStateService.getCampaignState(CampaignStateName.AUDIT));

    Audit audit =
        Audit.builder()
            .auditor(auditorAuth)
            .campaign(campaign)
            .requestState(requestStateService.getRequestState(RequestStateName.PENDING))
            .build();

    return saveAndSendToBlockchain(audit);
  }

  @Override
  public Audit acceptCampaign(Long auditId) {
    Audit audit = findByIdOrThrowException(auditId);
    throwIfNoAccess(audit);

    campaignService.changeCampaignStateOrThrow(
        audit.getCampaign(), campaignStateService.getCampaignState(CampaignStateName.LAUNCH_READY));
    audit.setRequestState(requestStateService.getRequestState(RequestStateName.APPROVED));
    return saveAndSendToBlockchain(audit);
  }

  @Override
  public Audit declineCampaign(Long auditId, String content) {
    Audit audit = findByIdOrThrowException(auditId);
    throwIfNoAccess(audit);

    campaignService.changeCampaignStateOrThrow(
        audit.getCampaign(), campaignStateService.getCampaignState(CampaignStateName.INITIAL));
    audit.setContent(content);
    audit.setRequestState(requestStateService.getRequestState(RequestStateName.DECLINED));
    return saveAndSendToBlockchain(audit);
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
    return audit
        .getAuditor()
        .getId()
        .equals(AuthenticationUtil.getAuthentication().getAuth().getId());
  }

  private Audit saveAndSendToBlockchain(Audit audit) {
    audit = auditRepository.save(audit);

    blockchainCommunicationService.invoke(
        BlockchainMethod.CAMPAIGN_STATE_CHANGE,
        new ChangeStateDto(
            audit.getCampaign(), AuthenticationUtil.getAuthentication().getAuth().getId()),
        AuthenticationUtil.getClientIp());

    return audit;
  }
}
