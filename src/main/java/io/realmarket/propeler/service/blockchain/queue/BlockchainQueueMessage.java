package io.realmarket.propeler.service.blockchain.queue;

import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockchainQueueMessage {
  private String methodName;
  private AbstractBlockchainDto dto;
  private String username;
}
