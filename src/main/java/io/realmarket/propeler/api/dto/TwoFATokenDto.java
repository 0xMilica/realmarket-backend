package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel("Token and 2fa code for verification.")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TwoFATokenDto {

  @NotBlank(message = "Please provide token")
  private String token;

  private String code;

  private String wildcard;
}
