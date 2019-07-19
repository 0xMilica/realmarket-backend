package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@ApiModel(value = "PersonDto", description = "Off platform investment data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OffPlatformInvestmentRequestDto {

  @ApiModelProperty(value = "Person's identifier")
  private Long id;

  @ApiModelProperty(value = "Person's email")
  private String email;

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

  @ApiModelProperty(value = "Person's profile picture url")
  private String profilePictureUrl;

  @ApiModelProperty(value = "Invested amount")
  private BigDecimal investedAmount;
}
