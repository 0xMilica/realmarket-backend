package io.realmarket.propeler.unit.api.controller.impl;

import io.realmarket.propeler.api.controller.impl.AuthControllerImpl;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.unit.util.AuthUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static io.realmarket.propeler.unit.util.AuthUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class AuthControllerImplTest {
  @Mock private AuthService authService;

  @InjectMocks private AuthControllerImpl authControllerImpl;

  @Test
  public void Register_Should_ReturnCreated() {
    ResponseEntity responseEntity = authControllerImpl.register(TEST_REGISTRATION_DTO);

    verify(authService, Mockito.times(1)).register(TEST_REGISTRATION_DTO);
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
  }

  @Test
  public void ConfirmRegistration_Should_ReturnOK() {
    ResponseEntity responseEntity =
        authControllerImpl.confirmRegistration(TEST_CONFIRM_REGISTRATION_DTO);

    verify(authService, Mockito.times(1)).confirmRegistration(TEST_CONFIRM_REGISTRATION_DTO);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }

  @Test
  public void Login_Should_Return_CREATED() {
    ResponseEntity responseEntity = authControllerImpl.login(AuthUtils.TEST_LOGIN_DTO);

    verify(authService, Mockito.times(1)).login(TEST_LOGIN_DTO);
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
  }

  @Test(expected = BadCredentialsException.class)
  public void Login_Should_Throw_Exception() {
    when(authService.login(TEST_LOGIN_DTO)).thenThrow(BadCredentialsException.class);
    authControllerImpl.login(AuthUtils.TEST_LOGIN_DTO);
  }
}
