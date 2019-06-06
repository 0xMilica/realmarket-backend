package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.Campaign;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@ApiModel(description = "CampaignResponseDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignResponseDto {

  @ApiModelProperty(value = "Campaign company id")
  private Long companyId;

  @ApiModelProperty(value = "Campaign name")
  private String name;

  @ApiModelProperty(value = "Url friendly version of campaign name")
  private String urlFriendlyName;

  @ApiModelProperty(value = "Campaign funding goals")
  private Long fundingGoals;

  @ApiModelProperty(value = "Campaign funded amount")
  private Long fundedAmount;

  @ApiModelProperty(value = "Campaign time to raise funds")
  private Integer timeToRaiseFunds;

  @ApiModelProperty(value = "Time left till the end of campaign")
  private Integer timeLeft;

  @ApiModelProperty(value = "Campaign min equity offered")
  private BigDecimal minEquityOffered;

  @ApiModelProperty(value = "Campaign max equity offered")
  private BigDecimal maxEquityOffered;

  @ApiModelProperty(value = "Campaign minimal investment")
  private BigDecimal minInvestment;

  @ApiModelProperty(value = "Campaign market image url")
  private String marketImageUrl;

  @ApiModelProperty(value = "Campaign company logo url")
  private String companyLogoUrl;

  @ApiModelProperty(value = "Campaign tagline")
  private String tagLine;

  @ApiModelProperty(value = "Campaign company category")
  private String tag;

  @ApiModelProperty(value = "Campaign company location")
  private String location;

  @ApiModelProperty(value = "Campaign state")
  private String state;

  public CampaignResponseDto(Campaign campaign) {
    this.companyId = campaign.getCompany().getId();
    this.name = campaign.getName();
    this.urlFriendlyName = campaign.getUrlFriendlyName();
    this.fundingGoals = campaign.getFundingGoals();
    this.fundedAmount = 12345L;
    this.timeToRaiseFunds = campaign.getTimeToRaiseFunds();
    this.timeLeft = 15;
    this.minEquityOffered = campaign.getMinEquityOffered();
    this.maxEquityOffered = campaign.getMaxEquityOffered();
    this.minInvestment = campaign.getMinInvestment();
    this.marketImageUrl = campaign.getMarketImageUrl();
    this.companyLogoUrl = campaign.getCompany().getLogoUrl();
    this.tagLine = campaign.getTagLine();
    this.tag = campaign.getCompany().getCompanyCategory().getName();
    this.location = campaign.getCompany().getCity() + ", " + campaign.getCompany().getCounty();
    this.state = campaign.getCampaignState().getName().toString();
  }
}
