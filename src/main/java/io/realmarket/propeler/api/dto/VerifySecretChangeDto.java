package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "Create new secret request data")
public class VerifySecretChangeDto {

  @NotBlank(message = "Please provide authentication or wildcard code")
  @ApiModelProperty(value = "Two factor authentication or wildcard code")
  String twoFaCode;
}
