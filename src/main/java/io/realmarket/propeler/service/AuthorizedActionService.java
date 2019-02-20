package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.TwoFADto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.AuthorizedAction;
import io.realmarket.propeler.model.enums.EAuthorizationActionType;

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
      Long authId, EAuthorizationActionType type, String data, Long mmTimeout);

  AuthorizedAction findAuthorizedActionOrThrowException(Auth auth, EAuthorizationActionType type);

  void deleteByAuthAndType(Auth authId, EAuthorizationActionType type);

  /**
   * Validate code and return data if code is valid.
   *
   * @param auth Person that data belongs
   * @param type Type of data that we want to retrieve
   * @param twoFADto TwoFADto instance that contains code that will be validated
   * @return Optional data or empty if code is not valid.
   */
  Optional<String> validateAuthorizationAction(
      Auth auth, EAuthorizationActionType type, TwoFADto twoFADto);
}
