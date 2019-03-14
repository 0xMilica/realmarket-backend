package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.TwoFADto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.OTPWildcard;

import java.util.List;

public interface OTPService {
  /**
   * Generate new secret and store it to AuthorizedAction.
   *
   * @param auth Person that requests change of secret.
   * @return new secret.
   */
  String generateTOTPSecret(Auth auth);

  /**
   * Validate if temporary saved secret is valid with new code. If it is valid new secret will be
   * saved.
   *
   * @param auth Person that requested change of secret
   * @param code Code that corresponds with new secret
   * @return Return if code is valid with new secret.
   */
  Boolean validateTOTPSecretChange(Auth auth, String code);

  /**
   * Generate list of OTP recovery codes that can be used in place of TOTP code.
   *
   * @param auth Person that request OTP recovery code
   * @return list of recovery codes
   */
  List<String> generateRecoveryCodes(Auth auth);

  /**
   * Validate code
   *
   * @param auth Person that request code to be validated
   * @param twoFADto twoFADto to be validated
   * @return true if code is valid or false if not.
   */
  Boolean validate(Auth auth, TwoFADto twoFADto);

  /**
   * Get wildcards for a user
   *
   * @param auth Id of an auth that requestd codes
   */
  List<OTPWildcard> getWildcardsByAuthId(final Long authId);
}
