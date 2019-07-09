package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.CampaignApplicationDto;
import io.realmarket.propeler.model.CampaignApplication;

import java.math.BigDecimal;

public class CampaignApplicationUtil {
  public static final BigDecimal TEST_PREVIOUSLY_RAISED = BigDecimal.valueOf(150000);

  public static CampaignApplication TEST_PENDING_CAMPAIGN_APPLICATION =
      CampaignApplication.builder()
          .firstName(PersonUtils.TEST_PERSON_FIRST_NAME)
          .lastName(PersonUtils.TEST_PERSON_LAST_NAME)
          .companyName(CompanyUtils.TEST_NAME)
          .website(CompanyUtils.TEST_WEBSITE)
          .email(PersonUtils.TEST_NEW_EMAIL)
          .phoneNumber(PersonUtils.TEST_PERSON_PHONE_NUMBER)
          .previouslyRaised(TEST_PREVIOUSLY_RAISED)
          .fundingGoals(CampaignUtils.TEST_FUNDING_GOALS)
          .requestState(AuditUtils.TEST_PENDING_REQUEST_STATE)
          .build();

  public static CampaignApplicationDto TEST_CAMPAIGN_APPLICATION_DTO =
      CampaignApplicationDto.builder()
          .firstName(PersonUtils.TEST_PERSON_FIRST_NAME)
          .lastName(PersonUtils.TEST_PERSON_LAST_NAME)
          .companyName(CompanyUtils.TEST_NAME)
          .website(CompanyUtils.TEST_WEBSITE)
          .email(PersonUtils.TEST_NEW_EMAIL)
          .phoneNumber(PersonUtils.TEST_PERSON_PHONE_NUMBER)
          .previouslyRaised(TEST_PREVIOUSLY_RAISED)
          .fundingGoals(CampaignUtils.TEST_FUNDING_GOALS)
          .build();
}
