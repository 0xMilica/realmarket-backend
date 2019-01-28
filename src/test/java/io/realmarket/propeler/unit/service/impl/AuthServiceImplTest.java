package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.repository.AuthRepository;
import io.realmarket.propeler.service.impl.AuthServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static io.realmarket.propeler.unit.util.AuthUtils.TEST_AUTH;
import static io.realmarket.propeler.unit.util.AuthUtils.TEST_USERNAME;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class AuthServiceImplTest {

  @Mock private AuthRepository authRepository;

  @InjectMocks private AuthServiceImpl authService;

  @Test
  public void FindByUsernameOrThrowException_Should_ReturnAuth_IfUserExists() {
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(TEST_AUTH));

    Auth retVal = authService.findByUsernameOrThrowException(TEST_USERNAME);

    assertEquals(TEST_AUTH, retVal);
  }

  @Test(expected = EntityNotFoundException.class)
  public void FindByUsernameOrThrowException_Should_ThrowException_IfUserNotExists() {
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

    authService.findByUsernameOrThrowException(TEST_USERNAME);
  }
}
