package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Dto used for transfer of contract signer information")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractSignerResponseDto {
  @ApiModelProperty("Signer's auth id")
  Long authId;

  @ApiModelProperty("Has already signed the contract")
  boolean hasSigned;
}
