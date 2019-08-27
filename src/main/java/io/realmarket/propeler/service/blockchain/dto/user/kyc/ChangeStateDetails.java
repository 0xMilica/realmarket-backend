package io.realmarket.propeler.service.blockchain.dto.user.kyc;

import io.realmarket.propeler.model.UserKYC;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStateDetails {
  private String newState;
  private Long auditorId;
  private String rejectionReason;

  public ChangeStateDetails(UserKYC userKYC) {
    this.newState = userKYC.getRequestState().getName().toString();
    this.auditorId = userKYC.getAuditor() != null ? userKYC.getAuditor().getId() : null;
    this.rejectionReason = userKYC.getRejectionReason();
  }
}
