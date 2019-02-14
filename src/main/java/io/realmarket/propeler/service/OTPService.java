package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.enums.EAuthorizationActionType;
import java.util.List;
import java.util.Optional;

public interface OTPService {
  // Create secret
  String generateTOTPSecret(Long authId);

  /**
   * Generate new secret and store it to AuthorizedAction.
   * @param auth Person that requests change of secret.
   * @return new secret.
   */
  String generateTOTPSecret(Auth auth);

  /**
   * Validate if temporary saved secret is valid with new code. If it is valid new secret will be saved.
   * @param auth Person that requested change of secret
   * @param code Code that corresponds with new secret
   * @return  Return if code is valid with new secret.
   */
  Boolean validateTOTPSecretChange(Auth auth, String code);

  /** Generate list of OTP recovery codes that can be used in place of TOTP code.
   * @param auth Person that request OTP recovery code
   * @return list of recovery codes
   */
  List<String> generateRecoveryCodes(Auth auth);

  /**
   * Validate code
   * @param auth Person that request code to be validated
   * @param code Code to be validated
   * @return true if code is valid or false if not.
   */
  Boolean validate(Auth auth, String code);

  /** Store data for safe keeping. And request proper code to obtain data.
   * @param authId Person that request data tobe saved
   * @param type  Type of dataSafe
   * @param data  Data to be saved
   * @param mmTimeout Timeout in milliseconds
   */
  void storeAuthorizationAction(Long authId, EAuthorizationActionType type, String data, Long mmTimeout);

  /**
   * Validate code and return data if code is valid.
   * @param auth  Person that data belongs
   * @param type  Type of data that we want to retrieve
   * @param code  OTP code that will be validated
   * @return  Optional data or empty if code is not valid.
   */
  Optional<String> validateAuthorizationAction(Auth auth, EAuthorizationActionType type, String code);

}
