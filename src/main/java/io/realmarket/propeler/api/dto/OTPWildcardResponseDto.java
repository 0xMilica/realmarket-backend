package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel("Wildcard codes list response")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OTPWildcardResponseDto {

  @ApiModelProperty(value = "List of wildcard strings")
  private List<String> wildcards;
}
