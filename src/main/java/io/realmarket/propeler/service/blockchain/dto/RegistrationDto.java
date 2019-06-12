package io.realmarket.propeler.service.blockchain.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class RegistrationDto extends AbstractBlockchainDto {
  private String role;

  @Builder
  public RegistrationDto(Long userId, String IP, Long timestamp, String role) {
    super(userId, IP, timestamp);
    this.role = role;
  }
}
