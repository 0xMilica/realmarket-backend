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
  private Long authId;

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
  private String content;

  public UserKYCResponseDto(UserKYC userKYC, Person person, Auth auth, Company company) {
    this.id = userKYC.getId();
    this.auditorId = (userKYC.getAuditor() != null) ? userKYC.getAuditor().getId() : null;
    this.firstName = person.getFirstName();
    this.lastName = person.getLastName();
    this.authId = auth.getId();
    this.userName = auth.getUsername();
    if (auth.getUserRole().getName().equals(UserRoleName.ROLE_INVESTOR))
      this.userRole = "Individual investor";
    else if (auth.getUserRole().getName().equals(UserRoleName.ROLE_ENTREPRENEUR))
      this.userRole = "Entrepreneur";
    else this.userRole = "Wrong user buddy...";
    if (company == null) {
      this.companyId = null;
      this.companyName = null;
    } else {
      this.companyName = company.getName();
      this.companyId = company.getId();
    }
    this.requestState = userKYC.getRequestState().getName().toString();
    this.content = userKYC.getContent();
  }
}
