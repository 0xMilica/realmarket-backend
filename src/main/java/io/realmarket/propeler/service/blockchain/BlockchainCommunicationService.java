package io.realmarket.propeler.service.blockchain;

import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;

import java.util.Map;
import java.util.concurrent.Future;

public interface BlockchainCommunicationService {

  Future<Map<String, Object>> invoke(
      String methodName, AbstractBlockchainDto dto, String username, String ipAddress );
}
