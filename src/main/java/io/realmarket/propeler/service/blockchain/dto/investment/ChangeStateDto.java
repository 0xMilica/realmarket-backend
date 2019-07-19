package io.realmarket.propeler.service.blockchain.dto.investment;

import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.Data;

@Data
public class ChangeStateDto extends AbstractBlockchainDto {
  private Long investmentId;
  private String newState;

  public ChangeStateDto(Investment investment, Long userId) {
    this.userId = userId;
    this.investmentId = investment.getId();
    this.newState = investment.getInvestmentState().getName().toString();
  }
}
