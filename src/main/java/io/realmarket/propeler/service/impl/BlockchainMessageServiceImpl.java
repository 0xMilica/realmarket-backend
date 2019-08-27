package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.BlockchainMessage;
import io.realmarket.propeler.repository.BlockchainMessageRepository;
import io.realmarket.propeler.service.BlockchainMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockchainMessageServiceImpl implements BlockchainMessageService {
  private final BlockchainMessageRepository blockchainMessageRepository;

  @Autowired
  public BlockchainMessageServiceImpl(BlockchainMessageRepository blockchainMessageRepository) {
    this.blockchainMessageRepository = blockchainMessageRepository;
  }

  public void deleteById(Long id) {
    blockchainMessageRepository.deleteById(id);
  }

  public BlockchainMessage save(BlockchainMessage blockchainMessage) {
    return blockchainMessageRepository.save(blockchainMessage);
  }

  public List<BlockchainMessage> findAllOrderedById() {
    return blockchainMessageRepository.findAllByOrderByIdAsc();
  }
}
