package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "Create new secret request data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerateNewSecretDto {
  @NotBlank(message = "Please provide password")
  @ApiModelProperty(value = "Active password")
  String password;

  @NotBlank(message = "Please provide authentication or wildcard code")
  @ApiModelProperty(value = "Two factor authentication or wildcard code")
  TwoFADto twoFa;
}
