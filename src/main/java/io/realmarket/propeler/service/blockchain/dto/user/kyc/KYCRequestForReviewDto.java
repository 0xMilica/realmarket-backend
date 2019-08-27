package io.realmarket.propeler.service.blockchain.dto.user.kyc;

import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KYCRequestForReviewDto extends AbstractBlockchainDto {
  private RequestForReviewDetails requestForReviewDetails;

  public KYCRequestForReviewDto(UserKYC userKYC, Long userId) {
    this.requestForReviewDetails = new RequestForReviewDetails(userKYC);
    this.userId = userId;
  }
}
