package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@ApiModel(description = "Dto used for transfer of contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractResponseDto {

  @ApiModelProperty("Sides that are signing the contract")
  List<ContractSignerResponseDto> signers;

  @ApiModelProperty("URL to the contract file")
  private String url;

  @ApiModelProperty("Base64 encoded document")
  private String contract;

  /* TODO this @deprecated contructor should be removed when Contract model is implemented.*/
  @Deprecated
  public ContractResponseDto(String url, String contract) {
    this.url = url;
    this.contract = contract;
    List<ContractSignerResponseDto> signers = new ArrayList<ContractSignerResponseDto>();
    signers.add(new ContractSignerResponseDto(1L, true));
    signers.add(new ContractSignerResponseDto(2L, false));
    this.signers = signers;
  }
}
