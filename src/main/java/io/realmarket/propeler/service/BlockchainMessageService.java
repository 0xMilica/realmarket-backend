package io.realmarket.propeler.service;

import io.realmarket.propeler.model.BlockchainMessage;

import java.util.List;

public interface BlockchainMessageService {
  void deleteById(Long id);

  BlockchainMessage save(BlockchainMessage blockchainMessage);

  List<BlockchainMessage> findAllOrderedById();
}
