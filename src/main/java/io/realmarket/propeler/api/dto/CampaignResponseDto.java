package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.realmarket.propeler.model.Audit;
import io.realmarket.propeler.model.Campaign;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@ApiModel(description = "CampaignResponseDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignResponseDto {

  @ApiModelProperty(value = "Campaign auditor id")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long auditorId;

  @ApiModelProperty(value = "Audit id")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long auditId;

  @ApiModelProperty(value = "Campaign company id")
  private Long companyId;

  @ApiModelProperty(value = "Campaign name")
  private String name;

  @ApiModelProperty(value = "Url friendly version of campaign name")
  private String urlFriendlyName;

  @ApiModelProperty(value = "Campaign closing reason")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String closingReason;

  @ApiModelProperty(value = "Campaign funding goals")
  private Long fundingGoals;

  @ApiModelProperty(value = "Campaign collected amount")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private BigDecimal collectedAmount;

  @ApiModelProperty(value = "Campaign funded percentage")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private BigDecimal fundedPercentage;

  @ApiModelProperty(value = "Campaign time to raise funds (in days)")
  private Integer timeToRaiseFunds;

  @ApiModelProperty(value = "Time left till the end of campaign (in minutes)")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long timeLeft;

  @ApiModelProperty(value = "Campaign min equity offered")
  private BigDecimal minEquityOffered;

  @ApiModelProperty(value = "Campaign max equity offered")
  private BigDecimal maxEquityOffered;

  @ApiModelProperty(value = "Campaign price per share")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private BigDecimal pricePerShare;

  @ApiModelProperty(value = "Campaign minimal investment")
  private BigDecimal minInvestment;

  @ApiModelProperty(value = "Campaign market image url")
  private String marketImageUrl;

  @ApiModelProperty(value = "Campaign company logo url")
  private String companyLogoUrl;

  @ApiModelProperty(value = "Campaign featured image url")
  private String companyFeaturedImageUrl;

  @ApiModelProperty(value = "Campaign tag line")
  private String tagLine;

  @ApiModelProperty(value = "Campaign company category")
  private String tag;

  @ApiModelProperty(value = "Campaign company location")
  private String location;

  @ApiModelProperty(value = "Campaign state")
  private String state;

  @ApiModelProperty(value = "Campaign topics status")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Map<String, Boolean> topicStatus;

  @ApiModelProperty(value = "Campaign company website")
  private String companyWebsite;

  @ApiModelProperty(value = "Campaign company twitter url")
  private String companyTwitterUrl;

  @ApiModelProperty(value = "Campaign company facebook url")
  private String companyFacebookUrl;

  @ApiModelProperty(value = "Campaign company linkedin url")
  private String companyLinkedinUrl;

  @ApiModelProperty(value = "Campaign company custom url")
  private String companyCustomUrl;

  public CampaignResponseDto(Campaign campaign) {
    this.companyId = campaign.getCompany().getId();
    this.name = campaign.getName();
    this.urlFriendlyName = campaign.getUrlFriendlyName();
    this.closingReason = campaign.getClosingReason();
    this.fundingGoals = campaign.getFundingGoals();
    this.collectedAmount = campaign.getCollectedAmount();
    this.fundedPercentage =
        campaign
            .getCollectedAmount()
            .multiply(campaign.getMinEquityOffered())
            .divide(BigDecimal.valueOf(campaign.getFundingGoals()), 2, RoundingMode.FLOOR);
    this.timeToRaiseFunds = campaign.getTimeToRaiseFunds();
    this.minEquityOffered = campaign.getMinEquityOffered();
    this.maxEquityOffered = campaign.getMaxEquityOffered();
    this.pricePerShare =
        BigDecimal.valueOf(0.01)
            .multiply(BigDecimal.valueOf(campaign.getFundingGoals()))
            .divide(campaign.getMinEquityOffered(), 2, RoundingMode.FLOOR);
    this.minInvestment = campaign.getMinInvestment();
    this.marketImageUrl = campaign.getMarketImageUrl();
    this.companyLogoUrl = campaign.getCompany().getLogoUrl();
    this.companyFeaturedImageUrl = campaign.getCompany().getFeaturedImageUrl();
    this.tagLine = campaign.getTagLine();
    this.tag = campaign.getCompany().getCompanyCategory().getName();
    this.location = campaign.getCompany().getCity() + ", " + campaign.getCompany().getCounty();
    this.state = campaign.getCampaignState().getName().toString();
    this.companyWebsite = campaign.getCompany().getWebsite();
    this.companyTwitterUrl = campaign.getCompany().getTwitterUrl();
    this.companyFacebookUrl = campaign.getCompany().getFacebookUrl();
    this.companyLinkedinUrl = campaign.getCompany().getLinkedinUrl();
    this.companyCustomUrl = campaign.getCompany().getCustomUrl();

    if (campaign.getActivationDate() != null) {
      LocalDateTime start =
          LocalDateTime.ofInstant(campaign.getActivationDate(), ZoneId.of("Europe/Belgrade"));
      LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Europe/Belgrade"));
      LocalDateTime end = start.plusDays(campaign.getTimeToRaiseFunds());
      this.timeLeft = now.until(end, ChronoUnit.MILLIS);
    }
  }

  public CampaignResponseDto(Campaign campaign, Audit audit) {
    this.auditorId = audit.getAuditor().getId();
    this.auditId = audit.getId();
    this.companyId = campaign.getCompany().getId();
    this.name = campaign.getName();
    this.urlFriendlyName = campaign.getUrlFriendlyName();
    this.closingReason = campaign.getClosingReason();
    this.fundingGoals = campaign.getFundingGoals();
    this.timeToRaiseFunds = campaign.getTimeToRaiseFunds();
    this.minEquityOffered = campaign.getMinEquityOffered();
    this.maxEquityOffered = campaign.getMaxEquityOffered();
    this.minInvestment = campaign.getMinInvestment();
    this.marketImageUrl = campaign.getMarketImageUrl();
    this.companyLogoUrl = campaign.getCompany().getLogoUrl();
    this.companyFeaturedImageUrl = campaign.getCompany().getFeaturedImageUrl();
    this.tagLine = campaign.getTagLine();
    this.tag = campaign.getCompany().getCompanyCategory().getName();
    this.location = campaign.getCompany().getCity() + ", " + campaign.getCompany().getCounty();
    this.state = campaign.getCampaignState().getName().toString();
    this.companyWebsite = campaign.getCompany().getWebsite();
    this.companyTwitterUrl = campaign.getCompany().getTwitterUrl();
    this.companyFacebookUrl = campaign.getCompany().getFacebookUrl();
    this.companyLinkedinUrl = campaign.getCompany().getLinkedinUrl();
    this.companyCustomUrl = campaign.getCompany().getCustomUrl();
  }
}
