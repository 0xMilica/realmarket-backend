package io.realmarket.propeler.service.blockchain.dto.campaign;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignChangeStateDto extends AbstractBlockchainDto {
  private Long campaignId;
  private String newState;

  public CampaignChangeStateDto(Campaign campaign, Long userId) {
    this.userId = userId;
    this.campaignId = campaign.getId();
    this.newState = campaign.getCampaignState().getName().toString();
  }
}
