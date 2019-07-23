package io.realmarket.propeler.util;

import io.realmarket.propeler.model.RequestState;
import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.model.enums.RequestStateName;

public class KYCUtils {
  public static final RequestState TEST_PENDING_REQUEST_STATE =
      RequestState.builder().name(RequestStateName.PENDING).build();

  public static final UserKYC TEST_USER_KYC =
      UserKYC.builder()
          .person(PersonUtils.TEST_PERSON)
          .requestState(TEST_PENDING_REQUEST_STATE)
          .build();
}
