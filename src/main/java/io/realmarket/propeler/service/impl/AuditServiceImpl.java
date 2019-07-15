package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.AuditRequestDto;
import io.realmarket.propeler.api.dto.enums.EmailType;
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
import io.realmarket.propeler.service.util.MailContentHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.*;

@Service
public class AuditServiceImpl implements AuditService {

  private final AuditRepository auditRepository;
  private final AuthService authService;
  private final RequestStateService requestStateService;
  private final CampaignService campaignService;
  private final CampaignStateService campaignStateService;
  private final EmailService emailService;
  private final BlockchainCommunicationService blockchainCommunicationService;

  @Autowired
  public AuditServiceImpl(
      AuditRepository auditRepository,
      RequestStateService requestStateService,
      AuthService authService,
      @Lazy CampaignService campaignService,
      CampaignStateService campaignStateService,
      EmailService emailService,
      BlockchainCommunicationService blockchainCommunicationService) {
    this.auditRepository = auditRepository;
    this.requestStateService = requestStateService;
    this.authService = authService;
    this.campaignService = campaignService;
    this.campaignStateService = campaignStateService;
    this.emailService = emailService;
    this.blockchainCommunicationService = blockchainCommunicationService;
  }

  @Override
  public Audit findPendingAuditByCampaignOrThrowException(Campaign campaign) {
    return auditRepository
        .findPendingAuditByCampaign(campaign)
        .orElseThrow(() -> new EntityNotFoundException(PENDING_AUDIT_NOT_FOUND));
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
    return audit;
  }

  @Override
  public void sendAcceptCampaignEmail(Audit audit) {
    Auth campaignOwner = audit.getCampaign().getCompany().getAuth();

    emailService.sendMailToUser(
        new MailContentHolder(
            Arrays.asList(campaignOwner.getPerson().getEmail()),
            EmailType.ACCEPT_CAMPAIGN,
            Collections.unmodifiableMap(
                Stream.of(
                        new AbstractMap.SimpleEntry<>(
                            EmailServiceImpl.USERNAME, campaignOwner.getUsername()),
                        new AbstractMap.SimpleEntry<>(
                            EmailServiceImpl.CAMPAIGN, audit.getCampaign().getName()))
                    .collect(
                        Collectors.toMap(
                            AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)))));
  }

  @Override
  public Audit declineCampaign(Long auditId, String content) {
    Audit audit = findByIdOrThrowException(auditId);
    throwIfNoAccess(audit);

    campaignService.changeCampaignStateOrThrow(audit.getCampaign(), CampaignStateName.INITIAL);
    audit.setContent(content);
    audit.setRequestState(requestStateService.getRequestState(RequestStateName.DECLINED));
    audit = saveAndSendToBlockchain(audit);
    sendDeclineCampaignEmail(audit);
    return audit;
  }

  @Override
  public void sendDeclineCampaignEmail(Audit audit) {
    Auth campaignOwner = audit.getCampaign().getCompany().getAuth();
    Map<String, Object> parameters = new HashMap<>();
    parameters.put(EmailServiceImpl.USERNAME, campaignOwner.getUsername());
    parameters.put(EmailServiceImpl.CAMPAIGN, audit.getCampaign().getName());
    parameters.put(EmailServiceImpl.REJECTION_REASON, audit.getContent());

    emailService.sendMailToUser(
        new MailContentHolder(
            Arrays.asList(campaignOwner.getPerson().getEmail()),
            EmailType.REJECT_CAMPAIGN,
            parameters));
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
