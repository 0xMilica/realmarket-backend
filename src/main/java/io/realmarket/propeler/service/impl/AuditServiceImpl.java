package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.AuditAssignmentDto;
import io.realmarket.propeler.api.dto.enums.EmailType;
import io.realmarket.propeler.model.Audit;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.enums.CampaignStateName;
import io.realmarket.propeler.model.enums.NotificationType;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.AuditRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.blockchain.BlockchainMethod;
import io.realmarket.propeler.service.blockchain.dto.campaign.CampaignChangeStateDto;
import io.realmarket.propeler.service.blockchain.queue.BlockchainMessageProducer;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.MailContentHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.*;

@Service
public class AuditServiceImpl implements AuditService {

  private final AuditRepository auditRepository;
  private final AuthService authService;
  private final RequestStateService requestStateService;
  private final CampaignService campaignService;
  private final EmailService emailService;
  private final BlockchainMessageProducer blockchainMessageProducer;
  private final NotificationService notificationService;

  @Autowired
  public AuditServiceImpl(
      AuditRepository auditRepository,
      RequestStateService requestStateService,
      AuthService authService,
      @Lazy CampaignService campaignService,
      EmailService emailService,
      NotificationService notificationService,
      BlockchainMessageProducer blockchainMessageProducer) {
    this.auditRepository = auditRepository;
    this.requestStateService = requestStateService;
    this.authService = authService;
    this.campaignService = campaignService;
    this.emailService = emailService;
    this.notificationService = notificationService;
    this.blockchainMessageProducer = blockchainMessageProducer;
  }

  @Override
  public Audit assignAudit(AuditAssignmentDto auditAssignmentDto) {
    Auth auditorAuth = authService.findByIdOrThrowException(auditAssignmentDto.getAuditorId());
    // TODO: Change this condition when assigning become possible for other roles too
    if (!auditorAuth.getUserRole().getName().equals(UserRoleName.ROLE_ADMIN)) {
      throw new BadRequestException(USER_CAN_NOT_BE_AUDITOR);
    }
    Campaign campaign =
        campaignService.getCampaignByUrlFriendlyName(
            auditAssignmentDto.getCampaignUrlFriendlyName());
    campaignService.changeCampaignStateOrThrow(campaign, CampaignStateName.AUDIT);

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

    campaignService.changeCampaignStateOrThrow(audit.getCampaign(), CampaignStateName.LAUNCH_READY);
    audit.setRequestState(requestStateService.getRequestState(RequestStateName.APPROVED));
    audit = saveAndSendToBlockchain(audit);
    sendAcceptCampaignEmail(audit);
    Auth recipient = audit.getCampaign().getCompany().getAuth();
    notificationService.sendMessage(recipient, NotificationType.ACCEPT_CAMPAIGN, null, null);
    return audit;
  }

  @Override
  public void sendAcceptCampaignEmail(Audit audit) {
    Auth campaignOwner = audit.getCampaign().getCompany().getAuth();
    Map<String, Object> parameters = new HashMap<>();
    parameters.put(EmailServiceImpl.FIRST_NAME, campaignOwner.getPerson().getFirstName());
    parameters.put(EmailServiceImpl.LAST_NAME, campaignOwner.getPerson().getLastName());
    parameters.put(EmailServiceImpl.CAMPAIGN, audit.getCampaign().getName());

    emailService.sendMailToUser(
        new MailContentHolder(
            Collections.singletonList(campaignOwner.getPerson().getEmail()),
            EmailType.ACCEPT_CAMPAIGN,
            parameters));
  }

  @Override
  public Audit declineCampaign(Long auditId, String rejectionReason) {
    Audit audit = findByIdOrThrowException(auditId);
    throwIfNoAccess(audit);

    campaignService.changeCampaignStateOrThrow(audit.getCampaign(), CampaignStateName.INITIAL);
    audit.setRejectionReason(rejectionReason);
    audit.setRequestState(requestStateService.getRequestState(RequestStateName.DECLINED));
    audit = saveAndSendToBlockchain(audit);
    sendDeclineCampaignEmail(audit);
    Auth recipient = audit.getCampaign().getCompany().getAuth();
    notificationService.sendMessage(
        recipient, NotificationType.REJECT_CAMPAIGN, rejectionReason, null);
    return audit;
  }

  @Override
  public void sendDeclineCampaignEmail(Audit audit) {
    Auth campaignOwner = audit.getCampaign().getCompany().getAuth();
    Map<String, Object> parameters = new HashMap<>();
    parameters.put(EmailServiceImpl.FIRST_NAME, campaignOwner.getPerson().getFirstName());
    parameters.put(EmailServiceImpl.LAST_NAME, campaignOwner.getPerson().getLastName());
    parameters.put(EmailServiceImpl.CAMPAIGN, audit.getCampaign().getName());
    parameters.put(EmailServiceImpl.REJECTION_REASON, audit.getRejectionReason());

    emailService.sendMailToUser(
        new MailContentHolder(
            Collections.singletonList(campaignOwner.getPerson().getEmail()),
            EmailType.REJECT_CAMPAIGN,
            parameters));
  }

  @Override
  public Audit findPendingAuditByCampaignOrThrowException(Campaign campaign) {
    return auditRepository
        .findPendingAuditByCampaign(campaign)
        .orElseThrow(() -> new EntityNotFoundException(PENDING_AUDIT_NOT_FOUND));
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

    blockchainMessageProducer.produceMessage(
        BlockchainMethod.CAMPAIGN_STATE_CHANGE,
        new CampaignChangeStateDto(
            audit.getCampaign(), AuthenticationUtil.getAuthentication().getAuth().getId()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());

    return audit;
  }
}
