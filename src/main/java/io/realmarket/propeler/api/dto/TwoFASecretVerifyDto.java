package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel("Fields needed to verify two fa secret")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TwoFASecretVerifyDto {
  @ApiModelProperty(value = "TOTP code needed to verify that secret is shared")
  @NotBlank(message = "TOTP code is empty.")
  @NotBlank
  private String code;

  @ApiModelProperty(value = "Token needed for verification of passed login.")
  @NotBlank(message = "Setup token is empty.")
  @NotBlank
  private String token;
}
