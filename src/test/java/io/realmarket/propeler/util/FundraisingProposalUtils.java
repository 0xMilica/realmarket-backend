package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.FundraisingProposalDto;
import io.realmarket.propeler.api.dto.RejectionReasonDto;
import io.realmarket.propeler.model.FundraisingProposal;

public class FundraisingProposalUtils {
  public static final Long TEST_ID = 1L;
  public static final Long TEST_FUNDRAISING_PROPOSAL_ID = 1L;
  public static final String TEST_CONTENT = "TEST_CONTENT";
  public static final String TEST_PREVIOUSLY_RAISED = "150000";
  public static final String TEST_FUNDING_GOALS = "200000";
  public static final String TEST_REJECTION_REASON = "TEST_REJECTION_REASON";
  public static final RejectionReasonDto TEST_AUDIT_DECLINE_DTO =
      RejectionReasonDto.builder().content(TEST_CONTENT).build();

  public static final FundraisingProposal TEST_PENDING_FUNDRAISING_PROPOSAL =
      FundraisingProposal.builder()
          .firstName(PersonUtils.TEST_PERSON_FIRST_NAME)
          .lastName(PersonUtils.TEST_PERSON_LAST_NAME)
          .companyName(CompanyUtils.TEST_NAME)
          .website(CompanyUtils.TEST_WEBSITE)
          .email(PersonUtils.TEST_NEW_EMAIL)
          .phoneNumber(PersonUtils.TEST_PERSON_PHONE_NUMBER)
          .previouslyRaised(TEST_PREVIOUSLY_RAISED)
          .fundingGoals(TEST_FUNDING_GOALS)
          .requestState(AuditUtils.TEST_PENDING_REQUEST_STATE)
          .build();
  public static final FundraisingProposal TEST_DECLINED_FUNDRAISING_PROPOSAL =
      FundraisingProposal.builder()
          .firstName(PersonUtils.TEST_PERSON_FIRST_NAME)
          .lastName(PersonUtils.TEST_PERSON_LAST_NAME)
          .companyName(CompanyUtils.TEST_NAME)
          .website(CompanyUtils.TEST_WEBSITE)
          .email(PersonUtils.TEST_NEW_EMAIL)
          .phoneNumber(PersonUtils.TEST_PERSON_PHONE_NUMBER)
          .previouslyRaised(TEST_PREVIOUSLY_RAISED)
          .fundingGoals(TEST_FUNDING_GOALS)
          .requestState(AuditUtils.TEST_DECLINED_REQUEST_STATE)
          .rejectionReason(TEST_REJECTION_REASON)
          .build();
  public static final FundraisingProposalDto TEST_FUNDRAISING_PROPOSAL_DTO =
      FundraisingProposalDto.builder()
          .firstName(PersonUtils.TEST_PERSON_FIRST_NAME)
          .lastName(PersonUtils.TEST_PERSON_LAST_NAME)
          .companyName(CompanyUtils.TEST_NAME)
          .website(CompanyUtils.TEST_WEBSITE)
          .email(PersonUtils.TEST_NEW_EMAIL)
          .phoneNumber(PersonUtils.TEST_PERSON_PHONE_NUMBER)
          .previouslyRaised(TEST_PREVIOUSLY_RAISED)
          .fundingGoals(TEST_FUNDING_GOALS)
          .build();
  public static FundraisingProposal TEST_APPROVED_FUNDRAISING_PROPOSAL =
      FundraisingProposal.builder()
          .firstName(PersonUtils.TEST_PERSON_FIRST_NAME)
          .lastName(PersonUtils.TEST_PERSON_LAST_NAME)
          .companyName(CompanyUtils.TEST_NAME)
          .website(CompanyUtils.TEST_WEBSITE)
          .email(PersonUtils.TEST_NEW_EMAIL)
          .phoneNumber(PersonUtils.TEST_PERSON_PHONE_NUMBER)
          .previouslyRaised(TEST_PREVIOUSLY_RAISED)
          .fundingGoals(TEST_FUNDING_GOALS)
          .requestState(AuditUtils.TEST_APPROVED_REQUEST_STATE)
          .build();
}
