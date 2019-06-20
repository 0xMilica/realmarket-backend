package io.realmarket.propeler.service.blockchain.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class RegenerationOfRecoveryDto extends AbstractBlockchainDto {

  @Builder
  public RegenerationOfRecoveryDto(Long userId, String IP, Long timestamp) {
    super(userId, IP, timestamp);
  }
}