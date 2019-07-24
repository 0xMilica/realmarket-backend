package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.UserKYCAssignmentDto;
import io.realmarket.propeler.model.RequestState;
import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.model.enums.RequestStateName;

public class KYCUtils {
  public static final RequestState TEST_PENDING_REQUEST_STATE =
      RequestState.builder().name(RequestStateName.PENDING).build();
  public static final Long TEST_USER_KYC_ID = 1L;

  public static final UserKYC TEST_USER_KYC =
      UserKYC.builder()
          .id(TEST_USER_KYC_ID)
          .person(PersonUtils.TEST_PERSON)
          .requestState(TEST_PENDING_REQUEST_STATE)
          .build();

  public static final UserKYC TEST_USER_KYC_ASSIGNED =
      UserKYC.builder()
          .id(TEST_USER_KYC_ID)
          .person(PersonUtils.TEST_PERSON)
          .requestState(TEST_PENDING_REQUEST_STATE)
          .auditor(AuthUtils.TEST_AUTH)
          .build();

  public static final UserKYCAssignmentDto TEST_USER_KYC_ASSIGNMENT_DTO =
      UserKYCAssignmentDto.builder()
          .auditorId(AuthUtils.TEST_AUTH_ID)
          .userKYCId(TEST_USER_KYC_ID)
          .build();
}
