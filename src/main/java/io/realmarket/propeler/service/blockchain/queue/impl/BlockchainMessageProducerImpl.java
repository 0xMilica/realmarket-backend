package io.realmarket.propeler.service.blockchain.queue.impl;

import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import io.realmarket.propeler.service.blockchain.queue.BlockchainMessageProducer;
import io.realmarket.propeler.service.blockchain.queue.BlockchainQueueMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.concurrent.BlockingQueue;

@Component
@Slf4j
public class BlockchainMessageProducerImpl implements BlockchainMessageProducer {

  @Resource(name = "blockchainMessageQueue")
  private BlockingQueue<BlockchainQueueMessage> blockchainMessageQueue;

  @Value("${blockchain.active}")
  private boolean active;

  public void produceMessage(
      String methodName, AbstractBlockchainDto dto, String username, String ipAddress) {
    if (!active) {
      log.info("Blockchain is off.");
    }

    dto.setIP(ipAddress);
    dto.setTimestamp(Instant.now().toEpochMilli());

    blockchainMessageQueue.add(new BlockchainQueueMessage(methodName, dto, username));
  }
}
