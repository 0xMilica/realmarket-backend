package io.realmarket.propeler.service.blockchain.dto.campaign;

import io.realmarket.propeler.model.Campaign;
import lombok.Data;

import java.time.Instant;

@Data
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
  private Instant activationTime;
  private Double collectedAmount;

  public CampaignDetails(Campaign campaign) {
    this.setCampaignId(campaign.getId());
    this.setCompanyId(campaign.getCompany().getId());
    this.setName(campaign.getName());
    this.setFriendlyUrl(campaign.getUrlFriendlyName());
    this.setTimeToRaiseFunds(campaign.getTimeToRaiseFunds());
    this.setMinEquityOffered(
        campaign.getMinEquityOffered() == null
            ? null
            : campaign.getMinEquityOffered().doubleValue());
    this.setMaxEquityOffered(
        campaign.getMaxEquityOffered() == null
            ? null
            : campaign.getMaxEquityOffered().doubleValue());
    this.setMinInvestmentLevel(
        campaign.getMinInvestment() == null ? null : campaign.getMinInvestment().doubleValue());
    this.setState(campaign.getCampaignState().getName().toString());
    this.setTagLine(campaign.getTagLine());
    this.setActivationTime(campaign.getActivationDate());
    this.setCollectedAmount(
        campaign.getCollectedAmount() == null ? null : campaign.getCollectedAmount().doubleValue());
  }
}
