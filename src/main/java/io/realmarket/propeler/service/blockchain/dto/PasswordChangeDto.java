package io.realmarket.propeler.service.blockchain.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class PasswordChangeDto extends AbstractBlockchainDto {

  @Builder
  public PasswordChangeDto(Long userId, String IP, Long timestamp) {
    super(userId, IP, timestamp);
  }
}
