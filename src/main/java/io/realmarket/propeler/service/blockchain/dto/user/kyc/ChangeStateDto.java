package io.realmarket.propeler.service.blockchain.dto.user.kyc;

import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.Data;

@Data
public class ChangeStateDto extends AbstractBlockchainDto {
  private Long userKYCId;
  private ChangeStateDetails changeStateDetails;

  public ChangeStateDto(UserKYC userKYC, Long userId) {
    this.userId = userId;
    this.userKYCId = userKYC.getId();
    this.changeStateDetails = new ChangeStateDetails(userKYC);
  }
}
