package io.realmarket.propeler.service.blockchain.dto;

import io.realmarket.propeler.model.Auth;
import lombok.Builder;
import lombok.Data;

@Data
public class RegistrationDto extends AbstractBlockchainDto {
  private String role;
  private String username;
  private HashedPersonDetails person;

  @Builder
  public RegistrationDto(
      Long userId,
      String IP,
      Long timestamp,
      String role,
      String username,
      HashedPersonDetails person) {
    super(userId, IP, timestamp);
    this.role = role;
    this.username = username;
    this.person = person;
  }

  public RegistrationDto(Auth auth) {
    this.role = auth.getUserRole().getName().toString();
    this.username = auth.getUsername();
    this.person = new HashedPersonDetails(auth.getPerson());
    this.userId = auth.getId();
  }
}
