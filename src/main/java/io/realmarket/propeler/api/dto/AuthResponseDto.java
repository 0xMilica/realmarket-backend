package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.api.dto.enums.E2FAStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Response dto returned on calling auth endpoint for login")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDto {
  @ApiModelProperty(value = "Status of authentication.")
  private E2FAStatus twoFAStatus;

  @ApiModelProperty(value = "Token needed for next action. Usage depends on twoFA status")
  private String token;
}
