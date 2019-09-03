package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Audit;
import io.realmarket.propeler.model.enums.NotificationType;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.AuditRepository;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.blockchain.queue.BlockchainMessageProducer;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.util.AuthUtils;
import io.realmarket.propeler.util.CampaignUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static io.realmarket.propeler.util.AuditUtils.*;
import static io.realmarket.propeler.util.AuthUtils.TEST_AUTH;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AuditServiceImpl.class)
public class AuditServiceImplTest {

  @Mock private RequestStateService requestStateService;
  @Mock private AuthService authService;
  @Mock private CampaignService campaignService;
  @Mock private EmailService emailService;
  @Mock private AuditRepository auditRepository;
  @Mock private NotificationService notificationService;
  @Mock private BlockchainMessageProducer blockchainMessageProducer;

  @InjectMocks private AuditServiceImpl auditServiceImpl;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContextAdmin();
  }

  @Test
  public void assignAudit_Should_Assign() {
    when(authService.findByIdOrThrowException(AuthUtils.TEST_AUTH_ID))
        .thenReturn(AuthUtils.TEST_AUTH_ADMIN);
    when(campaignService.getCampaignByUrlFriendlyName(CampaignUtils.TEST_URL_FRIENDLY_NAME))
        .thenReturn(CampaignUtils.TEST_REVIEW_READY_CAMPAIGN);
    when(requestStateService.getRequestState(RequestStateName.PENDING))
        .thenReturn(TEST_PENDING_REQUEST_STATE);
    when(auditRepository.save(any(Audit.class))).thenReturn(TEST_PENDING_REQUEST_AUDIT);

    Audit actualAudit = auditServiceImpl.assignAudit(AUDIT_REQUEST_DTO);

    assertEquals(TEST_PENDING_REQUEST_AUDIT, actualAudit);
  }

  @Test
  public void acceptCampaign_Should_Accept() {
    Audit audit = TEST_PENDING_REQUEST_AUDIT.toBuilder().build();
    when(auditRepository.findById(TEST_AUDIT_ID)).thenReturn(Optional.of(audit));
    when(requestStateService.getRequestState(RequestStateName.APPROVED))
        .thenReturn(TEST_APPROVED_REQUEST_STATE);
    when(auditRepository.save(audit)).thenReturn(TEST_APPROVED_REQUEST_AUDIT);
    doNothing().when(emailService).sendEmailToUser(any(), any(), any(), any());
    doNothing()
        .when(notificationService)
        .sendMessage(TEST_AUTH, NotificationType.ACCEPT_CAMPAIGN, null, null);
    Audit actualAudit = auditServiceImpl.acceptCampaign(1L);

    assertEquals(TEST_APPROVED_REQUEST_STATE, actualAudit.getRequestState());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void acceptCampaign_Should_Throw_When_Not_Campaign_Auditor() {
    AuthUtils.mockRequestAndContext();
    when(auditRepository.findById(TEST_AUDIT_ID))
        .thenReturn(Optional.of(TEST_PENDING_REQUEST_AUDIT));

    auditServiceImpl.acceptCampaign(1L);
  }

  @Test
  public void declineCampaign_Should_Decline() {
    Audit audit = TEST_PENDING_REQUEST_AUDIT.toBuilder().build();
    when(auditRepository.findById(TEST_AUDIT_ID)).thenReturn(Optional.of(audit));
    when(requestStateService.getRequestState(RequestStateName.DECLINED))
        .thenReturn(TEST_DECLINED_REQUEST_STATE);
    when(auditRepository.save(audit)).thenReturn(TEST_DECLINED_REQUEST_AUDIT);
    doNothing().when(emailService).sendEmailToUser(any(), any(), any(), any());
    doNothing()
        .when(notificationService)
        .sendMessage(TEST_AUTH, NotificationType.REJECT_CAMPAIGN, null, null);
    Audit actualAudit = auditServiceImpl.declineCampaign(1L, REJECTION_REASON);

    assertEquals(TEST_DECLINED_REQUEST_STATE, actualAudit.getRequestState());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void declineCampaign_Should_Throw_When_Not_Campaign_Auditor() {
    AuthUtils.mockRequestAndContext();
    when(auditRepository.findById(TEST_AUDIT_ID))
        .thenReturn(Optional.of(TEST_PENDING_REQUEST_AUDIT));

    auditServiceImpl.declineCampaign(1L, REJECTION_REASON);
  }
}
