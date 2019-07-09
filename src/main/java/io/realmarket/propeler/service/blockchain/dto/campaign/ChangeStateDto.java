package io.realmarket.propeler.service.blockchain.dto.campaign;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.Data;

@Data
public class ChangeStateDto extends AbstractBlockchainDto {
  private Long campaignId;
  private String newState;

  public ChangeStateDto(Campaign campaign, Long userId) {
    this.userId = userId;
    this.campaignId = campaign.getId();
    this.newState = campaign.getCampaignState().getName().toString();
  }
}
