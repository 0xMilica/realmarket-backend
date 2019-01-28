package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.api.annotations.Email;
import io.realmarket.propeler.model.enums.EUserRole;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel("Encapsulates registration information.")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationDto {

  @Email
  @NotNull(message = "Please provide e-mail address")
  private String email;

  @NotNull(message = "Please provide username")
  private String username;

  @NotNull(message = "Please provide password")
  @Size(min = 8, message = "Password must be at least 8 characters long.")
  private String password;

  @NotNull private EUserRole userRole;

  @NotNull(message = "Please provide first name")
  private String firstName;

  @NotNull(message = "Please provide last name")
  private String lastName;

  @NotNull(message = "Please provide country of residence")
  private String countryOfResidence;

  private String countryForTaxation;

  @NotNull(message = "Please provide city of residence")
  private String city;

  @NotNull(message = "Please provide address")
  private String address;

  private String phoneNumber;
}
