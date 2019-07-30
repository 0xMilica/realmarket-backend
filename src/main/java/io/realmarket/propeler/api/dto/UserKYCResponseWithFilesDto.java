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

import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserKYCResponseWithFilesDto extends UserKYCResponseDto {

  @ApiModelProperty(value = "User's personal id's front url")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private List<DocumentResponseDto> userDocuments;

  public UserKYCResponseWithFilesDto(
      UserKYC userKYC,
      Person person,
      Auth user,
      Company company,
      List<DocumentResponseDto> userDocuments) {
    super(userKYC, person, user, company);
    this.userDocuments = userDocuments;
  }
}
