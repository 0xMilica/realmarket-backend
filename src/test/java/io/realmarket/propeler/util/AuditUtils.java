package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.AuditAssignmentDto;
import io.realmarket.propeler.model.Audit;
import io.realmarket.propeler.model.RequestState;
import io.realmarket.propeler.model.enums.RequestStateName;

public class AuditUtils {

  public static final Long TEST_AUDIT_ID = 1L;
  public static final RequestState TEST_PENDING_REQUEST_STATE =
      RequestState.builder().name(RequestStateName.PENDING).build();
  public static final RequestState TEST_APPROVED_REQUEST_STATE =
      RequestState.builder().name(RequestStateName.APPROVED).build();
  public static final RequestState TEST_DECLINED_REQUEST_STATE =
      RequestState.builder().name(RequestStateName.DECLINED).build();
  public static final String REJECTION_REASON = "Submitted campaign did not pass our standards";

  public static final AuditAssignmentDto AUDIT_REQUEST_DTO =
      AuditAssignmentDto.builder()
          .auditorId(AuthUtils.TEST_AUTH_ID)
          .campaignUrlFriendlyName(CampaignUtils.TEST_URL_FRIENDLY_NAME)
          .build();

  public static final Audit TEST_PENDING_REQUEST_AUDIT =
      Audit.builder()
          .id(TEST_AUDIT_ID)
          .auditor(AuthUtils.TEST_AUTH_ADMIN)
          .campaign(CampaignUtils.TEST_REVIEW_READY_CAMPAIGN)
          .requestState(TEST_PENDING_REQUEST_STATE)
          .build();

  public static final Audit TEST_APPROVED_REQUEST_AUDIT =
      Audit.builder()
          .id(TEST_AUDIT_ID)
          .auditor(AuthUtils.TEST_AUTH_ADMIN)
          .campaign(CampaignUtils.TEST_REVIEW_READY_CAMPAIGN)
          .requestState(TEST_APPROVED_REQUEST_STATE)
          .build();

  public static final Audit TEST_DECLINED_REQUEST_AUDIT =
      Audit.builder()
          .id(TEST_AUDIT_ID)
          .auditor(AuthUtils.TEST_AUTH_ADMIN)
          .campaign(CampaignUtils.TEST_REVIEW_READY_CAMPAIGN)
          .requestState(TEST_DECLINED_REQUEST_STATE)
          .build();
}
