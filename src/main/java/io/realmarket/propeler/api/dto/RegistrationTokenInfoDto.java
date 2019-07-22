package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "RegistrationTokenInfoDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationTokenInfoDto {

  private String firstName;

  private String lastName;
}
