package io.realmarket.propeler.service.blockchain.dto.investment;

import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentChangeStateDto extends AbstractBlockchainDto {
  private Long investmentId;
  private String newState;

  public InvestmentChangeStateDto(Investment investment, Long userId) {
    this.userId = userId;
    this.investmentId = investment.getId();
    this.newState = investment.getInvestmentState().getName().toString();
  }
}
