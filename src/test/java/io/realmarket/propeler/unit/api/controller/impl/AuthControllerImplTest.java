package io.realmarket.propeler.unit.api.controller.impl;

import io.realmarket.propeler.api.controller.impl.AuthControllerImpl;
import io.realmarket.propeler.service.AuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static io.realmarket.propeler.unit.util.AuthUtils.TEST_REGISTRATION_DTO;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;

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
}
