package io.realmarket.propeler.service.blockchain.dto.user.kyc;

import io.realmarket.propeler.model.UserKYC;
import lombok.Data;

@Data
public class RequestForReviewDetails {
  private Long userKYCId;
  private String requestState;

  public RequestForReviewDetails(UserKYC userKYC) {
    this.userKYCId = userKYC.getId();
    this.requestState = userKYC.getRequestState().getName().toString();
  }
}
