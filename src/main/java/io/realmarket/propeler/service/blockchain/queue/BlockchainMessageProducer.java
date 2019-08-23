package io.realmarket.propeler.service.blockchain.queue;

import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;

public interface BlockchainMessageProducer {
  void produceMessage(
      String methodName, AbstractBlockchainDto dto, String username, String ipAddress);
}
