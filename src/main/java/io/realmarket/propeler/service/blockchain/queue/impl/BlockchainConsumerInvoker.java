package io.realmarket.propeler.service.blockchain.queue.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.realmarket.propeler.model.BlockchainMessage;
import io.realmarket.propeler.service.BlockchainMessageService;
import io.realmarket.propeler.service.blockchain.queue.BlockchainMessageDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Component
public class BlockchainConsumerInvoker {

  private final BlockchainMessageConsumerImpl blockchainMessageConsumer;

  @Resource(name = "blockchainMessageQueue")
  private BlockingQueue<BlockchainMessage> blockchainMessageQueue;

  private BlockchainMessageService blockchainMessageService;

  @Value("${blockchain.active}")
  private boolean active;

  @Autowired
  public BlockchainConsumerInvoker(
      BlockchainMessageConsumerImpl blockchainMessageConsumer,
      BlockchainMessageService blockchainMessageService) {
    this.blockchainMessageConsumer = blockchainMessageConsumer;
    this.blockchainMessageService = blockchainMessageService;
  }

  @Async
  @EventListener(ApplicationReadyEvent.class)
  public void invokeMessageConsumer() {
    if (!active) {
      log.info("Blockchain is off.");

    } else {
      BlockchainMessage message = null;
      do {
        try {
          message = blockchainMessageQueue.take();

          blockchainMessageConsumer.processMessage(
              new ObjectMapper()
                  .readValue(message.getMessageDetails(), BlockchainMessageDetails.class));

        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          log.error("Thread interrupted - failed to take message from queue.");
          break;

        } catch (IOException e) {
          log.error(
              "Could not deserialize JSON from string, message removed from queue and deleted; message: {}",
              message.getMessageDetails());
        }

        blockchainMessageService.deleteById(message.getId());
      } while (true);
    }
  }
}
