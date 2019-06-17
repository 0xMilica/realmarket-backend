package io.realmarket.propeler.service.blockchain;

import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;

import java.util.Map;

public interface BlockchainCommunicationService {

  Map<String, Object> invoke(String methodName, AbstractBlockchainDto dto);
}
