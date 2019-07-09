package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.FundraisingProposalDto;
import io.realmarket.propeler.model.FundraisingProposal;

import java.math.BigDecimal;

public class FundraisingProposalUtil {
  public static final BigDecimal TEST_PREVIOUSLY_RAISED = BigDecimal.valueOf(150000);

  public static FundraisingProposal TEST_PENDING_FUNDRAISING_PROPOSAL =
      FundraisingProposal.builder()
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

  public static FundraisingProposalDto TEST_FUNDRAISING_PROPOSAL_DTO =
      FundraisingProposalDto.builder()
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
