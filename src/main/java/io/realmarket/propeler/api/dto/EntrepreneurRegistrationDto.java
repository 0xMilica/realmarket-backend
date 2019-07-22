package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "EntrepreneurRegistrationDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntrepreneurRegistrationDto extends RegistrationDto {

  @NotBlank(message = "Please provide registration token")
  private String registrationToken;

  @Builder(builderMethodName = "entrepreneurRegistrationDtoBuilder")
  public EntrepreneurRegistrationDto(
      String registrationToken,
      String email,
      String username,
      String password,
      String firstName,
      String lastName,
      String countryOfResidence,
      String countryForTaxation,
      String city,
      String address,
      String phoneNumber) {
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
    this.registrationToken = registrationToken;
  }
}
