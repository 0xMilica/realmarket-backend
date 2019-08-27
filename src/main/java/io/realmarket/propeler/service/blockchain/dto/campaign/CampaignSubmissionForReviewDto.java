package io.realmarket.propeler.service.blockchain.dto.campaign;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignSubmissionForReviewDto extends AbstractBlockchainDto {
  private CampaignDetails campaign;

  public CampaignSubmissionForReviewDto(Campaign campaign) {
    this.userId = campaign.getCompany().getAuth().getId();
    this.campaign = new CampaignDetails(campaign);
  }
}
