package io.realmarket.propeler.unit.api.controller.impl;

import io.realmarket.propeler.api.controller.impl.UserControllerImpl;
import io.realmarket.propeler.service.AuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;

import static io.realmarket.propeler.unit.util.AuthUtils.TEST_AUTH;
import static io.realmarket.propeler.unit.util.AuthUtils.TEST_USERNAME;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class UserControllerImplTest {

  @Mock private AuthService authService;

  @InjectMocks private UserControllerImpl userController;

  @Test
  public void UserExists_Should_ReturnOK_IfUserExists() {
    when(authService.findByUsernameOrThrowException(TEST_USERNAME)).thenReturn(TEST_AUTH);

    ResponseEntity<Void> retVal = userController.userExists(TEST_USERNAME);

    assertEquals(HttpStatus.OK, retVal.getStatusCode());
  }

  @Test(expected = EntityNotFoundException.class)
  public void UserExists_Should_ThrowException_IfUserNotExists() {
    when(authService.findByUsernameOrThrowException(TEST_USERNAME))
        .thenThrow(EntityNotFoundException.class);

    userController.userExists(TEST_USERNAME);
  }
}
