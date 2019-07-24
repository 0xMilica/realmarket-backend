package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.UserKYCRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.PersonService;
import io.realmarket.propeler.service.RequestStateService;
import io.realmarket.propeler.util.AuthUtils;
import io.realmarket.propeler.util.PersonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.realmarket.propeler.util.AuditUtils.TEST_PENDING_REQUEST_STATE;
import static io.realmarket.propeler.util.KYCUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserKYCServiceImpl.class)
public class UserKYCServiceImplTest {

  @Mock private RequestStateService requestStateService;
  @Mock private UserKYCRepository userKYCRepository;
  @Mock private PersonService personService;
  @Mock private AuthService authService;

  @InjectMocks private UserKYCServiceImpl userKYCService;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContext();
  }

  @Test
  public void createUserKYCRequest_Should_Create_UserKYCRequest() {
    when(personService.getPersonFromAuth(AuthenticationUtil.getAuthentication().getAuth()))
        .thenReturn(PersonUtils.TEST_PERSON);
    when(requestStateService.getRequestState(RequestStateName.PENDING))
        .thenReturn(TEST_PENDING_REQUEST_STATE);
    when(userKYCRepository.save(any(UserKYC.class))).thenReturn(TEST_USER_KYC);

    UserKYC actualUserKYC = userKYCService.createUserKYCRequest();

    assertEquals(RequestStateName.PENDING, actualUserKYC.getRequestState().getName());
    assertEquals(PersonUtils.TEST_PERSON_ID, actualUserKYC.getPerson().getId());
  }

  @Test
  public void assignUserKYC_Should_Assign() {
    when(authService.findByIdOrThrowException(AuthUtils.TEST_AUTH_ID))
        .thenReturn(AuthUtils.TEST_AUTH_ADMIN);
    when(userKYCRepository.getOne(TEST_USER_KYC_ID)).thenReturn(TEST_USER_KYC.toBuilder().build());
    when(requestStateService.getRequestState(RequestStateName.PENDING))
        .thenReturn(TEST_PENDING_REQUEST_STATE);
    when(userKYCRepository.save(any(UserKYC.class))).thenReturn(TEST_USER_KYC_ASSIGNED);

    UserKYC actualUserKYC = userKYCService.assignUserKYC(TEST_USER_KYC_ASSIGNMENT_DTO);

    assertEquals(AuthUtils.TEST_AUTH_ID, actualUserKYC.getAuditor().getId());
  }
}
