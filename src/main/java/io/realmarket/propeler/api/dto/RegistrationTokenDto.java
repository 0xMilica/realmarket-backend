package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "RegistrationTokenDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationTokenDto {

  @NotBlank(message = "Please provide token value")
  @ApiModelProperty(value = "Token value")
  String value;
}
