package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@ApiModel(description = "Dto used for sending needed data for generating contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractRequestDto {
  @ApiModelProperty("List of document signers")
  List<Long> signers;

  @ApiModelProperty("Additional data needed for getting/generating contracts")
  Map<String, Object> additionalData;
}
