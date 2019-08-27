package io.realmarket.propeler.service.blockchain.dto.user;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegistrationDto extends AbstractBlockchainDto {
  private String role;
  private String username;
  private HashedPersonDetails person;

  @Builder
  public UserRegistrationDto(
      Long userId,
      String IP,
      Long timestamp,
      String role,
      String username,
      HashedPersonDetails person) {
    super(userId, IP, timestamp, UserRegistrationDto.class.getSimpleName());
    this.role = role;
    this.username = username;
    this.person = person;
  }

  public UserRegistrationDto(Auth auth) {
    this.role = auth.getUserRole().getName().toString();
    this.username = auth.getUsername();
    this.person = new HashedPersonDetails(auth.getPerson());
    this.userId = auth.getId();
  }
}
