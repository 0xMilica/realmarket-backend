package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.Shareholder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@ApiModel(description = "Dto used for transfer of shareholder data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareholderRequestDto {
  @ApiModelProperty(value = "Is shareholder anonymous")
  private Boolean isAnonymous;

  @ApiModelProperty(value = "Name of shareholder")
  private String name;

  @ApiModelProperty(value = "Location of shareholder")
  private String location;

  @ApiModelProperty(value = "Invested amount")
  private BigDecimal investedAmount;

  @ApiModelProperty(value = "Short description of shareholder")
  private String description;

  @ApiModelProperty(value = "Photo url")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String photoUrl;

  @ApiModelProperty(value = "Linkedin url")
  private String linkedinUrl;

  @ApiModelProperty(value = "Twitter url")
  private String twitterUrl;

  @ApiModelProperty(value = "Facebook url")
  private String facebookUrl;

  @ApiModelProperty(value = "Custom url")
  private String customProfileUrl;

  @ApiModelProperty(value = "Is this shareholder corporate")
  private boolean isCompany;

  @ApiModelProperty(value = "Corporate shareholder company identification number")
  private String companyIdentificationNumber;

  @ApiModelProperty(value = "Order in shareholder list")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Integer orderNumber;

  public ShareholderRequestDto(Shareholder shareholder) {
    this.isAnonymous = shareholder.getIsAnonymous();
    this.name = shareholder.getName();
    this.location = shareholder.getLocation();
    this.investedAmount = shareholder.getInvestedAmount();
    this.description = shareholder.getDescription();
    this.photoUrl = shareholder.getPhotoUrl();
    this.linkedinUrl = shareholder.getLinkedinUrl();
    this.twitterUrl = shareholder.getTwitterUrl();
    this.facebookUrl = shareholder.getFacebookUrl();
    this.customProfileUrl = shareholder.getCustomProfileUrl();
    this.orderNumber = shareholder.getOrderNumber();
    this.companyIdentificationNumber = shareholder.getCompanyIdentificationNumber();
    this.isCompany = shareholder.isCompany();
  }

  public Shareholder createShareholder(Company company) {
    return Shareholder.builder()
        .company(company)
        .customProfileUrl(customProfileUrl)
        .description(description)
        .facebookUrl(facebookUrl)
        .linkedinUrl(linkedinUrl)
        .twitterUrl(twitterUrl)
        .investedAmount(investedAmount)
        .isAnonymous(isAnonymous)
        .location(location)
        .name(name)
        .photoUrl(photoUrl)
        .orderNumber(orderNumber)
        .isCompany(isCompany)
        .companyIdentificationNumber(companyIdentificationNumber)
        .build();
  }
}
