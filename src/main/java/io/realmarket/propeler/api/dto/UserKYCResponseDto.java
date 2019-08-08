package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.model.enums.UserRoleName;
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
public class UserKYCResponseDto {

  @ApiModelProperty(value = "UserKYC's identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @ApiModelProperty(value = "User's first name")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String firstName;

  @ApiModelProperty(value = "User's last name")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String lastName;

  @ApiModelProperty(value = "Platform's user identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long userId;

  @ApiModelProperty(value = "User's username")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String userName;

  @ApiModelProperty(value = "User's role")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String userRole;

  @ApiModelProperty(value = "User's company name")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String companyName;

  @ApiModelProperty(value = "User's company id")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long companyId;

  @ApiModelProperty(value = "Auditor's identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long auditorId;

  @ApiModelProperty(value = "UserKYC's state")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String requestState;

  @ApiModelProperty(value = "Reason for transition to DECLINED state, when declining User's KYC.")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String rejectionReason;

  @ApiModelProperty(value = "User politically exposed")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private boolean politicallyExposed;

  public UserKYCResponseDto(UserKYC userKYC, Person person, Auth user, Company company) {
    this.id = userKYC.getId();
    this.auditorId = (userKYC.getAuditor() != null) ? userKYC.getAuditor().getId() : null;
    this.firstName = person.getFirstName();
    this.lastName = person.getLastName();
    this.userId = user.getId();
    this.userName = user.getUsername();
    this.userRole = user.getUserRole().getName().toString();
    if (company == null) {
      this.companyId = null;
      if (user.getUserRole().getName().equals(UserRoleName.ROLE_COMPANY_INVESTOR)) {
        this.companyName = person.getCompanyName();
      } else {
        this.companyName = null;
      }
    } else {
      this.companyName = company.getName();
      this.companyId = company.getId();
    }
    this.requestState = userKYC.getRequestState().getName().toString();
    this.rejectionReason = userKYC.getRejectionReason();
    this.politicallyExposed = userKYC.isPoliticallyExposed();
  }
}
