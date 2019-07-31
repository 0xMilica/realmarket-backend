package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.UserKYCRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.EmailService;
import io.realmarket.propeler.service.PersonService;
import io.realmarket.propeler.service.RequestStateService;
import io.realmarket.propeler.service.blockchain.BlockchainCommunicationService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.util.AuthUtils;
import io.realmarket.propeler.util.PersonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static io.realmarket.propeler.util.AuditUtils.TEST_PENDING_REQUEST_STATE;
import static io.realmarket.propeler.util.KYCUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserKYCServiceImpl.class)
public class UserKYCServiceImplTest {

  @Mock private RequestStateService requestStateService;
  @Mock private UserKYCRepository userKYCRepository;
  @Mock private PersonService personService;
  @Mock private AuthService authService;
  @Mock private EmailService emailService;
  @Mock private BlockchainCommunicationService blockchainCommunicationService;

  @InjectMocks private UserKYCServiceImpl userKYCService;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContext();
    ReflectionTestUtils.setField(userKYCService, "timeZone", "Europe/Belgrade");
  }

  @Test
  public void createUserKYCRequest_Should_Create_UserKYCRequest() {
    when(personService.getPersonFromAuth(AuthenticationUtil.getAuthentication().getAuth()))
        .thenReturn(PersonUtils.TEST_PERSON);
    when(requestStateService.getRequestState(RequestStateName.PENDING))
        .thenReturn(TEST_PENDING_REQUEST_STATE);
    when(userKYCRepository.save(any(UserKYC.class))).thenReturn(TEST_PENDING_USER_KYC);

    UserKYC actualUserKYC = userKYCService.createUserKYCRequest();

    assertEquals(RequestStateName.PENDING, actualUserKYC.getRequestState().getName());
    assertEquals(AuthUtils.TEST_AUTH_ENTREPRENEUR.getId(), actualUserKYC.getUser().getId());
  }

  @Test
  public void assignUserKYC_Should_Assign() {
    AuthUtils.mockRequestAndContextAdmin();
    when(userKYCRepository.getOne(TEST_USER_KYC_ID))
        .thenReturn(TEST_PENDING_USER_KYC.toBuilder().build());
    when(requestStateService.getRequestState(RequestStateName.PENDING))
        .thenReturn(TEST_PENDING_REQUEST_STATE);
    when(userKYCRepository.save(any(UserKYC.class))).thenReturn(TEST_USER_KYC_ASSIGNED);
    doNothing().when(emailService).sendMailToUser(any());

    UserKYC actualUserKYC = userKYCService.assignUserKYC(TEST_USER_KYC_ASSIGNMENT_DTO);

    assertEquals(AuthUtils.TEST_AUTH_ADMIN_ID, actualUserKYC.getAuditor().getId());
  }

  @Test
  public void acceptUserKYC_Should_AcceptUserKYC() {
    AuthUtils.mockRequestAndContextAdmin();

    when(userKYCRepository.findById(TEST_USER_KYC_ID))
        .thenReturn(Optional.of(TEST_USER_KYC_ASSIGNED.toBuilder().build()));
    when(requestStateService.getRequestState(RequestStateName.APPROVED))
        .thenReturn(TEST_APPROVED_REQUEST_STATE);
    when(userKYCRepository.save(any())).thenReturn(TEST_USER_KYC_APPROVED);
    doNothing().when(emailService).sendMailToUser(any());

    UserKYC userKYC = userKYCService.approveUserKYC(TEST_USER_KYC_ID);

    assertEquals(TEST_APPROVED_REQUEST_STATE, userKYC.getRequestState());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void acceptUserKYC_Should_Throw_When_Not_UserKYC_Auditor() {
    AuthUtils.mockRequestAndContext();
    when(userKYCRepository.findById(TEST_USER_KYC_ID))
        .thenReturn(Optional.of(TEST_USER_KYC_ASSIGNED));

    userKYCService.approveUserKYC(TEST_USER_KYC_ID);
  }

  @Test
  public void declineUserKYC_Should_DeclineUserKYC() {
    AuthUtils.mockRequestAndContextAdmin();

    when(userKYCRepository.findById(TEST_USER_KYC_ID))
        .thenReturn(Optional.of(TEST_USER_KYC_ASSIGNED.toBuilder().build()));
    when(requestStateService.getRequestState(RequestStateName.DECLINED))
        .thenReturn(TEST_DECLINED_REQUEST_STATE);
    when(userKYCRepository.save(any())).thenReturn(TEST_USER_KYC_DECLINED);
    doNothing().when(emailService).sendMailToUser(any());

    UserKYC userKYC = userKYCService.rejectUserKYC(TEST_USER_KYC_ID, TEST_REJECTION_REASON);

    assertEquals(TEST_DECLINED_REQUEST_STATE, userKYC.getRequestState());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void declineUserKYCCampaign_Should_Throw_When_Not_UserKYC_Auditor() {
    AuthUtils.mockRequestAndContext();
    when(userKYCRepository.findById(TEST_USER_KYC_ID))
        .thenReturn(Optional.of(TEST_USER_KYC_ASSIGNED));

    userKYCService.rejectUserKYC(TEST_USER_KYC_ID, TEST_REJECTION_REASON);
  }
}
