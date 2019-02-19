package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.api.dto.TwoFASecretRequestDto;
import io.realmarket.propeler.api.dto.TwoFATokenDto;

public class TwoFactorAuthUtils {

  public static String TEST_SETUP_TOKEN = "SETUP_TOKEN";

  public static TwoFASecretRequestDto TEST_2FA_SECRET_REQUEST =
      TwoFASecretRequestDto.builder().setupToken(TEST_SETUP_TOKEN).build();

  public static TwoFATokenDto TEST_TWO_FA_TOKEN =
      new TwoFATokenDto(
          TemporaryTokenUtils.TEST_TEMPORARY_2FA_SETUP_TOKEN.getValue(),
          OTPUtils.TEST_TOTP_CODE_1,
          OTPUtils.TEST_OTP_WILDCARD_1);
}
