package io.realmarket.propeler.service.blockchain.dto.user;

import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.Builder;
import lombok.Data;

@Data
public class EmailChangeDto extends AbstractBlockchainDto {
  private String newEmailHash;

  @Builder
  public EmailChangeDto(Long userId, String IP, Long timestamp, String newEmailHash) {
    super(userId, IP, timestamp);
    this.newEmailHash = newEmailHash;
  }
}
