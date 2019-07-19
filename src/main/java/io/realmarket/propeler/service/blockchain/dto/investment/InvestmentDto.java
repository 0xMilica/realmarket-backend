package io.realmarket.propeler.service.blockchain.dto.investment;

import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.Data;

@Data
public class InvestmentDto extends AbstractBlockchainDto {
  private InvestmentDetails investment;

  public InvestmentDto(Investment investment) {
    this.userId = investment.getAuth().getId();
    this.investment = new InvestmentDetails(investment);
  }
}
