package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

@ApiModel(value = "OffPlatformInvestmentRequestDto", description = "Off platform investment data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OffPlatformInvestmentRequestDto {

  @ApiModelProperty(value = "Person's email")
  private String email;

  @ApiModelProperty(value = "Person's first name")
  private String firstName;

  @ApiModelProperty(value = "Person's last name")
  private String lastName;

  @ApiModelProperty(value = "Person's national identification number")
  private String nationalIdentificationNumber;

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

  @ApiModelProperty(value = "Linkedin url")
  private String linkedinUrl;

  @ApiModelProperty(value = "Twitter url")
  private String twitterUrl;

  @ApiModelProperty(value = "Facebook url")
  private String facebookUrl;

  @ApiModelProperty(value = "Custom url")
  private String customProfileUrl;

  @ApiModelProperty(value = "Short Biography")
  @Size(max = 250, message = "Short biography cannot be longer than 250 characters.")
  private String shortBiography;

  @ApiModelProperty(value = "Invested amount")
  private BigDecimal investedAmount;
}
