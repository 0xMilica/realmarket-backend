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
import java.util.Map;

@ApiModel(description = "AuditCampaignResponseDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditCampaignResponseDto {

  @ApiModelProperty(value = "Campaign auditor id")
  private Long auditorId;

  @ApiModelProperty(value = "Audit id")
  private Long auditId;

  @ApiModelProperty(value = "Campaign company id")
  private Long companyId;

  @ApiModelProperty(value = "Campaign name")
  private String name;

  @ApiModelProperty(value = "Url friendly version of campaign name")
  private String urlFriendlyName;

  @ApiModelProperty(value = "Campaign funding goals")
  private Long fundingGoals;

  @ApiModelProperty(value = "Campaign time to raise funds (in days)")
  private Integer timeToRaiseFunds;

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

  public AuditCampaignResponseDto(Audit audit, Campaign campaign) {
    this.auditorId = audit.getAuditor().getId();
    this.auditId = audit.getId();
    this.companyId = campaign.getCompany().getId();
    this.name = campaign.getName();
    this.urlFriendlyName = campaign.getUrlFriendlyName();
    this.fundingGoals = campaign.getFundingGoals();
    this.timeToRaiseFunds = campaign.getTimeToRaiseFunds();
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
