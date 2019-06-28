package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.TwoFADto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.AuthorizedAction;
import io.realmarket.propeler.model.enums.AuthorizedActionTypeName;

import java.util.Optional;

public interface AuthorizedActionService {
  /**
   * Store data for safe keeping. And request proper code to obtain data.
   *
   * @param authId Person that request data tobe saved
   * @param type Type of dataSafe
   * @param data Data to be saved
   * @param mmTimeout Timeout in milliseconds
   */
  void storeAuthorizationAction(
      Long authId, AuthorizedActionTypeName type, String data, Long mmTimeout);

  AuthorizedAction findAuthorizedActionOrThrowException(Auth auth, AuthorizedActionTypeName type);

  void deleteByAuthAndType(Auth authId, AuthorizedActionTypeName type);

  /**
   * Validate code and return data if code is valid.
   *
   * @param auth Person that data belongs
   * @param type Type of data that we want to retrieve
   * @param twoFADto TwoFADto instance that contains code that will be validated
   * @return Optional data or empty if code is not valid.
   */
  Optional<String> validateAuthorizationAction(
      Auth auth, AuthorizedActionTypeName type, TwoFADto twoFADto);
}
