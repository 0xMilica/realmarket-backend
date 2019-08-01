package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.UserKYCAssignmentDto;
import io.realmarket.propeler.api.dto.UserKYCRequestDto;
import io.realmarket.propeler.model.RequestState;
import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.model.enums.RequestStateName;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class KYCUtils {
  public static final Long TEST_USER_KYC_ID = 1L;
  public static final RequestState TEST_PENDING_REQUEST_STATE =
      RequestState.builder().name(RequestStateName.PENDING).build();
  public static final RequestState TEST_APPROVED_REQUEST_STATE =
      RequestState.builder().name(RequestStateName.APPROVED).build();
  public static final RequestState TEST_DECLINED_REQUEST_STATE =
      RequestState.builder().name(RequestStateName.DECLINED).build();
  public static final String TEST_REJECTION_REASON = "Submitted KYC did not pass our standards";
  public static final Instant TEST_DATE = Instant.now();
  public static final List<String> TEST_KYC_DOCUMENT_LIST = Arrays.asList("doc1.png", "doc2.png");
  public static final boolean TEST_POLITICALLY_EXPOSED = true;

  public static final UserKYC TEST_PENDING_USER_KYC =
      UserKYC.builder()
          .id(TEST_USER_KYC_ID)
          .user(AuthUtils.TEST_AUTH_ENTREPRENEUR)
          .requestState(TEST_PENDING_REQUEST_STATE)
          .uploadDate(TEST_DATE)
          .build();

  public static final UserKYC TEST_USER_KYC_ASSIGNED =
      UserKYC.builder()
          .id(TEST_USER_KYC_ID)
          .user(AuthUtils.TEST_AUTH_ENTREPRENEUR)
          .requestState(TEST_PENDING_REQUEST_STATE)
          .auditor(AuthUtils.TEST_AUTH_ADMIN)
          .uploadDate(TEST_DATE)
          .build();

  public static final UserKYC TEST_USER_KYC_APPROVED =
      UserKYC.builder()
          .id(TEST_USER_KYC_ID)
          .user(AuthUtils.TEST_AUTH_ENTREPRENEUR)
          .requestState(TEST_APPROVED_REQUEST_STATE)
          .auditor(AuthUtils.TEST_AUTH_ADMIN)
          .uploadDate(TEST_DATE)
          .build();

  public static final UserKYC TEST_USER_KYC_DECLINED =
      UserKYC.builder()
          .id(TEST_USER_KYC_ID)
          .user(AuthUtils.TEST_AUTH_ENTREPRENEUR)
          .requestState(TEST_DECLINED_REQUEST_STATE)
          .auditor(AuthUtils.TEST_AUTH_ADMIN)
          .uploadDate(TEST_DATE)
          .build();

  public static final UserKYC TEST_PENDING_USER_KYC_INVESTOR =
      UserKYC.builder()
          .id(TEST_USER_KYC_ID)
          .user(AuthUtils.TEST_AUTH_INVESTOR)
          .requestState(TEST_PENDING_REQUEST_STATE)
          .uploadDate(TEST_DATE)
          .build();

  public static final UserKYCAssignmentDto TEST_USER_KYC_ASSIGNMENT_DTO =
      UserKYCAssignmentDto.builder()
          .auditorId(AuthUtils.TEST_AUTH_ID)
          .userKYCId(TEST_USER_KYC_ID)
          .build();

  public static final UserKYCRequestDto TEST_USER_KYC_REQUEST_DTO =
      UserKYCRequestDto.builder()
          .documentsUrl(TEST_KYC_DOCUMENT_LIST)
          .politicallyExposed(TEST_POLITICALLY_EXPOSED)
          .build();
}
