package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.service.util.dto.LoginResponseDto;

import static io.realmarket.propeler.unit.util.JWTUtils.TEST_JWT_VALUE;

public class TwoFactorAuthUtils {

  public static String TEST_SETUP_TOKEN = "SETUP_TOKEN";

  public static TwoFASecretRequestDto TEST_2FA_SECRET_REQUEST =
      TwoFASecretRequestDto.builder().setupToken(TEST_SETUP_TOKEN).build();

  public static TwoFATokenDto TEST_TWO_FA_TOKEN =
      new TwoFATokenDto(
          TemporaryTokenUtils.TEST_TEMPORARY_2FA_SETUP_TOKEN.getValue(),
          OTPUtils.TEST_TOTP_CODE_1,
          OTPUtils.TEST_OTP_WILDCARD_1);

  public static String TEST_2FA_CODE = "TEST_2FA_CODE";
  public static final TwoFADto TEST_2FA_DTO = new TwoFADto(TEST_2FA_CODE, "");
  public static LoginResponseDto TEST_LOGIN_RESPONSE_DTO = new LoginResponseDto(TEST_JWT_VALUE);

  public static TwoFASecretVerifyDto TEST_TWO_FA_SECRET_VERIFY_REQUEST =
      new TwoFASecretVerifyDto(
          OTPUtils.TEST_TOTP_CODE_1, TemporaryTokenUtils.TEST_TEMPORARY_2FA_SETUP_TOKEN.getValue());

  public static GenerateNewSecretDto NEW_SECRET_REQUEST_1_DTO =
      new GenerateNewSecretDto(
          AuthUtils.TEST_PASSWORD, new TwoFADto(OTPUtils.TEST_TOTP_CODE_1, null));

  public static GenerateNewSecretDto NEW_SECRET_REQUEST_2_DTO =
      new GenerateNewSecretDto(
          AuthUtils.TEST_PASSWORD_NEW, new TwoFADto(null, OTPUtils.TEST_OTP_WILDCARD_1));
}
