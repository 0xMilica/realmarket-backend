package io.realmarket.propeler.service.blockchain.dto.user;

import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserPasswordChangeDto extends AbstractBlockchainDto {

  @Builder
  public UserPasswordChangeDto(Long userId, String IP, Long timestamp) {
    super(userId, IP, timestamp, UserPasswordChangeDto.class.getSimpleName());
  }
}
