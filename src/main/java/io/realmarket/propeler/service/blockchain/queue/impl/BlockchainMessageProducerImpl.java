package io.realmarket.propeler.service.blockchain.queue.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.realmarket.propeler.model.BlockchainMessage;
import io.realmarket.propeler.service.BlockchainMessageService;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import io.realmarket.propeler.service.blockchain.queue.BlockchainMessageDetails;
import io.realmarket.propeler.service.blockchain.queue.BlockchainMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.concurrent.BlockingQueue;

@Component
@Slf4j
public class BlockchainMessageProducerImpl implements BlockchainMessageProducer {

  @Resource(name = "blockchainMessageQueue")
  private BlockingQueue<BlockchainMessage> blockchainMessageQueue;

  private BlockchainMessageService blockchainMessageService;

  @Value("${blockchain.active}")
  private boolean active;

  @Autowired
  public BlockchainMessageProducerImpl(BlockchainMessageService blockchainMessageService) {
    this.blockchainMessageService = blockchainMessageService;
  }

  public void produceMessage(
      String methodName, AbstractBlockchainDto dto, String username, String ipAddress) {
    if (!active) {
      log.info("Blockchain is off.");
    }

    dto.setIP(ipAddress);
    dto.setTimestamp(Instant.now().toEpochMilli());
    dto.setName(dto.getClass().getSimpleName());

    try {
      String messageAsString =
          new ObjectMapper()
              .writeValueAsString(new BlockchainMessageDetails(methodName, dto, username));

      BlockchainMessage blockchainMessage =
          blockchainMessageService.save(
              BlockchainMessage.builder().messageDetails(messageAsString).build());

      blockchainMessageQueue.add(blockchainMessage);

    } catch (JsonProcessingException e) {
      log.error("Could not serialize object to JSON; message not added to queue or DB");
    }
  }
}
