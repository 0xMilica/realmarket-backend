package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.Shareholder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@ApiModel(description = "Dto used for transfer of campaign shareholder data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareholderDto {

  @ApiModelProperty(value = "Shareholder id")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

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

  @ApiModelProperty(value = "Order in shareholder list")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Integer orderNumber;

  @ApiModelProperty(value = "Campaign where shareholder belongs")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long campaignId;

  public ShareholderDto(Shareholder shareholder) {
    this.id = shareholder.getId();
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
    this.campaignId = shareholder.getCampaign().getId();
  }

  public Shareholder createShareholder(Campaign campaign) {
    return Shareholder.builder()
        .campaign(campaign)
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
        .build();
  }
}
