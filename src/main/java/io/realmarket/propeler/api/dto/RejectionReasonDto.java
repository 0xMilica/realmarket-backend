package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RejectionReasonDto {
  @ApiModelProperty(value = "Reason for transition to rejected state.")
  @Size(max = 10000, message = "Reason content cannot be longer than 10000 characters.")
  private String content;
}
