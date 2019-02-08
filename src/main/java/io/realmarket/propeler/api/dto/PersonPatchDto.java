package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(
        value = "PersonPatchDto",
        description = "Person arguments to patch")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonPatchDto {

  @ApiModelProperty(value = "Person's first name")
  private String firstName;

  @ApiModelProperty(value = "Person's last name")
  private String lastName;

  @ApiModelProperty(value = "Person's country of residence")
  private String countryOfResidence;

  @ApiModelProperty(value = "Person's country for taxation")
  private String countryForTaxation;

  @ApiModelProperty(value = "Person's city")
  private String city;

  @ApiModelProperty(value = "Person's address")
  private String address;

  @ApiModelProperty(value = "Person's phone number")
  private String phoneNumber;
}
