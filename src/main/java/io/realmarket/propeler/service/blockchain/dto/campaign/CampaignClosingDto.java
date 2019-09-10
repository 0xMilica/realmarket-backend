package io.realmarket.propeler.service.blockchain.dto.campaign;

import io.realmarket.propeler.api.dto.CampaignClosingReasonDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignClosingDto extends AbstractBlockchainDto {
  private Long campaignId;
  private boolean successful;
  private String closingReason;

  public CampaignClosingDto(
      Campaign campaign, CampaignClosingReasonDto campaignClosingReasonDto, Long userId) {
    this.userId = userId;
    this.campaignId = campaign.getId();
    this.successful = campaignClosingReasonDto.isSuccessful();
    this.closingReason = campaignClosingReasonDto.getClosingReason();
  }
}
