package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "CorporateInvestorRegistrationDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorporateInvestorRegistrationDto extends RegistrationDto {

  @NotBlank(message = "Please provide company name")
  private String companyName;

  @NotBlank(message = "Please provide company identification number")
  private String companyIdentificationNumber;

  @Builder(builderMethodName = "corporateInvestorRegistrationDtoBuilder")
  public CorporateInvestorRegistrationDto(
      String email,
      String username,
      String password,
      String firstName,
      String lastName,
      String countryOfResidence,
      String countryForTaxation,
      String city,
      String address,
      String phoneNumber,
      String companyName,
      String companyIdentificationNumber) {
    super(
        email,
        username,
        password,
        firstName,
        lastName,
        countryOfResidence,
        countryForTaxation,
        city,
        address,
        phoneNumber);
    this.companyName = companyName;
    this.companyIdentificationNumber = companyIdentificationNumber;
  }
}
