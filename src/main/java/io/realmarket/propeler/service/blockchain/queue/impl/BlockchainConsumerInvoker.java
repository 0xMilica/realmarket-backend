package io.realmarket.propeler.service.blockchain.queue.impl;

import io.realmarket.propeler.service.blockchain.queue.BlockchainQueueMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Component
public class BlockchainConsumerInvoker {

  private final BlockchainMessageConsumerImpl blockchainMessageConsumer;

  @Resource(name = "blockchainMessageQueue")
  private BlockingQueue<BlockchainQueueMessage> blockchainMessageQueue;

  @Value("${blockchain.active}")
  private boolean active;

  @Autowired
  public BlockchainConsumerInvoker(BlockchainMessageConsumerImpl blockchainMessageConsumer) {
    this.blockchainMessageConsumer = blockchainMessageConsumer;
  }

  @Async
  @EventListener(ApplicationReadyEvent.class)
  public void invokeMessageConsumer() {
    if (!active) {
      log.info("Blockchain is off.");

    } else {
      BlockchainQueueMessage message;
      do {

        try {
          message = blockchainMessageQueue.take();

        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          log.error("Thread interrupted - failed to take message from queue.");
          break;
        }

        blockchainMessageConsumer.processMessage(message);
      } while (true);
    }
  }
}
