package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.UserKYCRepository;
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
import static io.realmarket.propeler.util.KYCUtils.TEST_USER_KYC;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserKYCServiceImpl.class)
public class UserKYCServiceImplTest {

  @Mock private RequestStateService requestStateService;
  @Mock private UserKYCRepository userKYCRepository;
  @Mock private PersonService personService;

  @InjectMocks private UserKYCServiceImpl userKYCService;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContext();
  }

  @Test
  public void createUserKYCRequest_Should_Create_UserKYCRequest() {
    when(personService.findByIdOrThrowException(PersonUtils.TEST_PERSON_ID))
        .thenReturn(PersonUtils.TEST_PERSON);
    when(requestStateService.getRequestState(RequestStateName.PENDING))
        .thenReturn(TEST_PENDING_REQUEST_STATE);
    when(userKYCRepository.save(TEST_USER_KYC)).thenReturn(TEST_USER_KYC);

    UserKYC actualUserKYC = userKYCService.createUserKYCRequest(PersonUtils.TEST_PERSON_ID);

    assertEquals(RequestStateName.PENDING, actualUserKYC.getRequestState().getName());
    assertEquals(PersonUtils.TEST_PERSON_ID, actualUserKYC.getPerson().getId());
  }
}
