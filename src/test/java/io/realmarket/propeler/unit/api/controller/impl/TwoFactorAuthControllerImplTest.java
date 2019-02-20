package io.realmarket.propeler.unit.api.controller.impl;

import io.realmarket.propeler.api.controller.impl.TwoFactorAuthControllerImpl;
import io.realmarket.propeler.service.TwoFactorAuthService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.dto.LoginResponseDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static io.realmarket.propeler.unit.util.TwoFactorAuthUtils.TEST_LOGIN_RESPONSE_DTO;
import static io.realmarket.propeler.unit.util.TwoFactorAuthUtils.TEST_TWO_FA_TOKEN;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class TwoFactorAuthControllerImplTest {

  @InjectMocks private TwoFactorAuthControllerImpl twoFactorAuthController;

  @Mock private TwoFactorAuthService twoFactorAuthService;

  @Test
  public void Login2FA_Should_Return_LoginResponseDto() {
    when(twoFactorAuthService.login2FA(TEST_TWO_FA_TOKEN)).thenReturn(TEST_LOGIN_RESPONSE_DTO);

    ResponseEntity<LoginResponseDto> retVal = twoFactorAuthController.login2FA(TEST_TWO_FA_TOKEN);

    assertEquals(HttpStatus.OK, retVal.getStatusCode());
    assertEquals(TEST_LOGIN_RESPONSE_DTO, retVal.getBody());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void Login2FA_Should_Return_Forbidden() {
    when(twoFactorAuthService.login2FA(TEST_TWO_FA_TOKEN))
        .thenThrow(ForbiddenOperationException.class);

    ResponseEntity<LoginResponseDto> retVal = twoFactorAuthController.login2FA(TEST_TWO_FA_TOKEN);

    assertEquals(HttpStatus.FORBIDDEN, retVal.getStatusCode());
  }
}
