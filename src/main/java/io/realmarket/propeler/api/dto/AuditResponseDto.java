package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.realmarket.propeler.model.Audit;
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
public class AuditResponseDto {

  @ApiModelProperty(value = "Audit's identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @ApiModelProperty(value = "Auditor's identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long auditorId;

  @ApiModelProperty(value = "Url friendly version of campaign name")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String campaignUrlFriendlyName;

  @ApiModelProperty(value = "Audit's state")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String request_state;

  @ApiModelProperty(value = "Reason for transition to REJECTED state, when declining campaign audit.")
  @Size(max = 10000, message = "Audit content cannot be longer than 10000 characters.")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String content;

  public AuditResponseDto(Audit audit) {
    this.id = audit.getId();
    this.auditorId = audit.getAuditorAuth().getId();
    this.campaignUrlFriendlyName = audit.getCampaign().getUrlFriendlyName();
    this.request_state = audit.getRequestState().toString();
    this.content = audit.getContent();
  }
}
