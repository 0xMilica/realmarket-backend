package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.Campaign;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignEmailDto {

  private String name;
  private String overview;
  private Long fundingGoals;
  private Integer timeToRaiseFunds;
  private BigDecimal minEquityOffered;
  private BigDecimal maxEquityOffered;
  private String link;

  public CampaignEmailDto(Campaign campaign, String frontendUrl, String overview) {
    this.name = campaign.getName();
    this.overview = overview;
    this.fundingGoals = campaign.getFundingGoals();
    this.timeToRaiseFunds = campaign.getTimeToRaiseFunds();
    this.minEquityOffered = campaign.getMinEquityOffered();
    this.maxEquityOffered = campaign.getMaxEquityOffered();
    this.link = frontendUrl + "/overview/" + campaign.getUrlFriendlyName();
  }
}
