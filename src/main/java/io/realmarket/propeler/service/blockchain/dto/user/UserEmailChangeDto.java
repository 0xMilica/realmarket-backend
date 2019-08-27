package io.realmarket.propeler.service.blockchain.dto.user;

import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserEmailChangeDto extends AbstractBlockchainDto {
  private String newEmailHash;

  @Builder
  public UserEmailChangeDto(Long userId, String IP, Long timestamp, String newEmailHash) {
    super(userId, IP, timestamp, UserEmailChangeDto.class.getSimpleName());
    this.newEmailHash = newEmailHash;
  }
}
