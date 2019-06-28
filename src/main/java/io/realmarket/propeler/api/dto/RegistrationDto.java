package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.api.annotations.Email;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel("Encapsulates registration information.")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationDto {

  @Email
  @NotBlank(message = "Please provide e-mail address")
  private String email;

  @NotBlank(message = "Please provide username")
  private String username;

  @NotBlank(message = "Please provide password")
  @Size(min = 8, message = "Password must be at least 8 characters long.")
  private String password;

  @NotNull private UserRoleName userRole;

  @NotBlank(message = "Please provide first name")
  private String firstName;

  @NotBlank(message = "Please provide last name")
  private String lastName;

  @NotBlank(message = "Please provide country of residence")
  private String countryOfResidence;

  private String countryForTaxation;

  @NotBlank(message = "Please provide city of residence")
  private String city;

  @NotBlank(message = "Please provide address")
  private String address;

  private String phoneNumber;
}
