package io.realmarket.propeler.service.blockchain.dto.user.kyc;

import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.Data;

@Data
public class RequestForReviewDto extends AbstractBlockchainDto {
  private RequestForReviewDetails requestForReviewDetails;

  public RequestForReviewDto(UserKYC userKYC, Long userId) {
    this.requestForReviewDetails = new RequestForReviewDetails(userKYC);
    this.userId = userId;
  }
}
