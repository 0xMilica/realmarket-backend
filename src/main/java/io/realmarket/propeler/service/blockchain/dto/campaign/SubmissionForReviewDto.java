package io.realmarket.propeler.service.blockchain.dto.campaign;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.Data;

@Data
public class SubmissionForReviewDto extends AbstractBlockchainDto {
  private CampaignDetails campaign;

  public SubmissionForReviewDto(Campaign campaign) {
    this.userId = campaign.getCompany().getAuth().getId();
    this.campaign = new CampaignDetails(campaign);
  }
}
