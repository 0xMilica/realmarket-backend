package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.primitives.UnsignedInteger;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignInvestor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@ApiModel(description = "Dto used for transfer of campaign investor data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CampaignInvestorDto {

  @ApiModelProperty(value = "Campaign investor id")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @ApiModelProperty(value = "Is investor anonymous")
  private Boolean isAnonymous;

  @ApiModelProperty(value = "Name of investor")
  private String name;

  @ApiModelProperty(value = "Location of investor")
  private String location;

  @ApiModelProperty(value = "Invested amount")
  private BigDecimal investedAmount;

  @ApiModelProperty(value = "Short description of investor")
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

  @ApiModelProperty(value = "Order in investor list")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Integer orderNumber;

  @ApiModelProperty(value = "Campaign where investor belongs")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long campaignId;

  public CampaignInvestorDto(CampaignInvestor campaignInvestor) {
    this.id = campaignInvestor.getId();
    this.isAnonymous = campaignInvestor.getIsAnonymous();
    this.name = campaignInvestor.getName();
    this.location = campaignInvestor.getLocation();
    this.investedAmount = campaignInvestor.getInvestedAmount();
    this.description = campaignInvestor.getDescription();
    this.photoUrl = campaignInvestor.getPhotoUrl();
    this.linkedinUrl = campaignInvestor.getLinkedinUrl();
    this.twitterUrl = campaignInvestor.getTwitterUrl();
    this.facebookUrl = campaignInvestor.getFacebookUrl();
    this.customProfileUrl = campaignInvestor.getCustomProfileUrl();
    this.orderNumber = campaignInvestor.getOrderNumber();
    this.campaignId = campaignInvestor.getCampaign().getId();
  }

  public CampaignInvestor createInvestor(Campaign campaign) {
    return CampaignInvestor.builder()
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
