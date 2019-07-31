package io.realmarket.propeler.model;

import io.realmarket.propeler.api.dto.FundraisingProposalDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "fundraising_proposal")
public class FundraisingProposal {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FUNDRAISING_PROPOSAL_SEQ")
  @SequenceGenerator(
      name = "FUNDRAISING_PROPOSAL_SEQ",
      sequenceName = "FUNDRAISING_PROPOSAL_SEQ",
      allocationSize = 1)
  private Long id;

  private String firstName;
  private String lastName;
  private String companyName;
  private String website;
  private String email;
  private String phoneNumber;
  private String previouslyRaised;
  private String fundingGoals;

  @JoinColumn(
      name = "requestStateId",
      foreignKey = @ForeignKey(name = "campaign_application_fk_on_request_state"))
  @ManyToOne
  private RequestState requestState;

  @Column(length = 10000)
  private String rejectionReason;

  public FundraisingProposal(FundraisingProposalDto fundraisingProposalDto) {
    this.firstName = fundraisingProposalDto.getFirstName();
    this.lastName = fundraisingProposalDto.getLastName();
    this.companyName = fundraisingProposalDto.getCompanyName();
    this.website = fundraisingProposalDto.getWebsite();
    this.email = fundraisingProposalDto.getEmail();
    this.phoneNumber = fundraisingProposalDto.getPhoneNumber();
    this.previouslyRaised = fundraisingProposalDto.getPreviouslyRaised();
    this.fundingGoals = fundraisingProposalDto.getFundingGoals();
  }
}
