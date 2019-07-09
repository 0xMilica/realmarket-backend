package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.api.annotations.Email;
import io.realmarket.propeler.model.CampaignApplication;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel(value = "Campaign fundraising application")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignApplicationDto {

  @NotBlank(message = "Please provide first name")
  private String firstName;

  @NotBlank(message = "Please provide last name")
  private String lastName;

  @ApiModelProperty(value = "Company's name")
  @NotBlank
  private String companyName;

  @ApiModelProperty(value = "Company's website")
  private String website;

  @Email
  @NotBlank(message = "Please provide e-mail address")
  private String email;

  @ApiModelProperty(value = "Person's phone number")
  private String phoneNumber;

  @NotNull
  @ApiModelProperty(value = "Previously raised amount of money")
  private BigDecimal previouslyRaised;

  @ApiModelProperty(value = "Funding goals")
  @NotNull(message = "Please provide funding goals")
  private Long fundingGoals;

  public CampaignApplicationDto(CampaignApplication campaignApplication) {
    this.firstName = campaignApplication.getFirstName();
    this.lastName = campaignApplication.getLastName();
    this.companyName = campaignApplication.getCompanyName();
    this.website = campaignApplication.getWebsite();
    this.email = campaignApplication.getEmail();
    this.phoneNumber = campaignApplication.getPhoneNumber();
    this.previouslyRaised = campaignApplication.getPreviouslyRaised();
    this.fundingGoals = campaignApplication.getFundingGoals();
  }
}
