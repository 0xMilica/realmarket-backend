package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditAssignmentDto {

  @ApiModelProperty(value = "Auditor's identifier")
  private Long auditorId;

  @ApiModelProperty(value = "Url friendly version of campaign name")
  private String campaignUrlFriendlyName;
}
