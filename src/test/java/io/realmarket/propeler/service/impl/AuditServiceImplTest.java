package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Audit;
import io.realmarket.propeler.repository.AuditRepository;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.RequestStateService;
import io.realmarket.propeler.util.AuthUtils;
import io.realmarket.propeler.util.CampaignUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.realmarket.propeler.util.AuditUtils.TEST_AUDIT;
import static io.realmarket.propeler.util.AuditUtils.TEST_AUDIT_REQUEST_DTO;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AuditServiceImpl.class)
public class AuditServiceImplTest {

  @Mock private RequestStateService requestStateService;

  @Mock private AuthService authService;

  @Mock private CampaignService campaignService;

  @Mock private AuditRepository auditRepository;

  @InjectMocks private AuditServiceImpl auditServiceImpl;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContextAdmin();
  }

  @Test
  public void assignAuditRequest_Should_Assign() {
    when(authService.findByIdOrThrowException(AuthUtils.TEST_AUTH_ID + 2))
        .thenReturn(AuthUtils.TEST_AUTH_ADMIN);
    when(campaignService.getCampaignByUrlFriendlyName(CampaignUtils.TEST_URL_FRIENDLY_NAME))
        .thenReturn(CampaignUtils.TEST_REVIEW_READY_CAMPAIGN);
    when(auditRepository.save(any(Audit.class))).thenReturn(TEST_AUDIT);

    Audit actualAudit = auditServiceImpl.assignAuditRequest(TEST_AUDIT_REQUEST_DTO);

    assertEquals(TEST_AUDIT, actualAudit);
  }
}
