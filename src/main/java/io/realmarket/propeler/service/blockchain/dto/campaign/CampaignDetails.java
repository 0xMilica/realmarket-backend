package io.realmarket.propeler.service.blockchain.dto.campaign;

import io.realmarket.propeler.model.Campaign;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDetails {
  private Long campaignId;
  private Long companyId;
  private String name;
  private String friendlyUrl;
  private Integer timeToRaiseFunds;
  private Double minEquityOffered;
  private Double maxEquityOffered;
  private Double minInvestmentLevel;
  private String state;
  private String tagLine;
  private String activationTime;
  private Double collectedAmount;
  private String currency;

  public CampaignDetails(Campaign campaign) {
    this.campaignId = campaign.getId();
    this.companyId = campaign.getCompany().getId();
    this.name = campaign.getName();
    this.friendlyUrl = campaign.getUrlFriendlyName();
    this.timeToRaiseFunds = campaign.getTimeToRaiseFunds();
    this.minEquityOffered =
        campaign.getMinEquityOffered() == null
            ? null
            : campaign.getMinEquityOffered().doubleValue();
    this.maxEquityOffered =
        campaign.getMaxEquityOffered() == null
            ? null
            : campaign.getMaxEquityOffered().doubleValue();
    this.minInvestmentLevel =
        campaign.getMinInvestment() == null ? null : campaign.getMinInvestment().doubleValue();
    this.state = campaign.getCampaignState().getName().toString();
    this.tagLine = campaign.getTagLine();
    this.activationTime =
        campaign.getActivationDate() == null ? null : campaign.getActivationDate().toString();
    this.collectedAmount =
        campaign.getCollectedAmount() == null ? null : campaign.getCollectedAmount().doubleValue();
    this.currency = campaign.getCurrency();
  }
}
