package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.api.annotations.Email;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "Fundraising proposal")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundraisingProposalDto {

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
  private String previouslyRaised;

  @ApiModelProperty(value = "Funding goals")
  @NotNull(message = "Please provide funding goals")
  private String fundingGoals;
}
