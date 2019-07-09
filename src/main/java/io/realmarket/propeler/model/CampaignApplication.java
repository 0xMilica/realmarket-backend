package io.realmarket.propeler.model;

import io.realmarket.propeler.api.dto.CampaignApplicationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "campaign_application")
public class CampaignApplication {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAMPAIGN_APPLICATION_SEQ")
  @SequenceGenerator(
      name = "CAMPAIGN_APPLICATION_SEQ",
      sequenceName = "CAMPAIGN_APPLICATION_SEQ",
      allocationSize = 1)
  private Long id;

  private String firstName;
  private String lastName;
  private String companyName;
  private String website;
  private String email;
  private String phoneNumber;
  private BigDecimal previouslyRaised;
  private Long fundingGoals;

  @JoinColumn(
      name = "requestStateId",
      foreignKey = @ForeignKey(name = "campaign_application_fk_on_request_state"))
  @ManyToOne
  private RequestState requestState;

  @Column(length = 10000)
  private String content;

  public CampaignApplication(CampaignApplicationDto campaignApplicationDto) {
    this.firstName = campaignApplicationDto.getFirstName();
    this.lastName = campaignApplicationDto.getLastName();
    this.companyName = campaignApplicationDto.getCompanyName();
    this.website = campaignApplicationDto.getWebsite();
    this.email = campaignApplicationDto.getEmail();
    this.phoneNumber = campaignApplicationDto.getPhoneNumber();
    this.previouslyRaised = campaignApplicationDto.getPreviouslyRaised();
    this.fundingGoals = campaignApplicationDto.getFundingGoals();
  }
}
