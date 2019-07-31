package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.realmarket.propeler.model.UserKYC;
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
public class UserKYCDto {

  @ApiModelProperty(value = "UserKYC's identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @ApiModelProperty(value = "Auditor's identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long auditorId;

  @ApiModelProperty(value = "User's identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long userId;

  @ApiModelProperty(value = "KYC's state")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String requestState;

  @ApiModelProperty(value = "Reason for transition to DECLINED state, when declining User's KYC.")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String rejectionReason;

  public UserKYCDto(UserKYC userKYC) {
    this.id = userKYC.getId();
    this.auditorId = (userKYC.getAuditor() != null) ? userKYC.getAuditor().getId() : null;
    this.userId = userKYC.getUser().getId();
    this.requestState = userKYC.getRequestState().getName().toString();
    this.rejectionReason = userKYC.getRejectionReason();
  }
}
