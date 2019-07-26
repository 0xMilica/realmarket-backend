package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.model.UserKYC;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserKYCResponseWithFilesDto extends UserKYCResponseDto {

  @ApiModelProperty(value = "User's personal id's front url")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String personalIdFrontURL;

  @ApiModelProperty(value = "User's personal id's back url")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String personalIdBackURL;

  public UserKYCResponseWithFilesDto(
      UserKYC userKYC,
      Person person,
      Auth auth,
      Company company,
      String personalIdBackURL,
      String personalIdFrontURL) {
    super(userKYC, person, auth, company);
    this.personalIdBackURL = personalIdBackURL;
    this.personalIdFrontURL = personalIdFrontURL;
  }
}
