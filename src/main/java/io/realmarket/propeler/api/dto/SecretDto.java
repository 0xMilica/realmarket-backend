package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "Newly generated secret and wildcard codes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecretDto {
  @NotBlank(message = "Newly generated secret code")
  @ApiModelProperty(value = "New secret")
  String secret;
}
