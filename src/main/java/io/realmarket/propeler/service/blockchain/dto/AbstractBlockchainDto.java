package io.realmarket.propeler.service.blockchain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class AbstractBlockchainDto {
  protected Long userId;
  protected String IP;
  protected Long timestamp;
}
