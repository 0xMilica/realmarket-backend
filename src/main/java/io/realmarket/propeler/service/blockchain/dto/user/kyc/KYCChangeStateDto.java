package io.realmarket.propeler.service.blockchain.dto.user.kyc;

import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KYCChangeStateDto extends AbstractBlockchainDto {
  private Long userKYCId;
  private ChangeStateDetails changeStateDetails;

  public KYCChangeStateDto(UserKYC userKYC, Long userId) {
    this.userId = userId;
    this.userKYCId = userKYC.getId();
    this.changeStateDetails = new ChangeStateDetails(userKYC);
  }
}
