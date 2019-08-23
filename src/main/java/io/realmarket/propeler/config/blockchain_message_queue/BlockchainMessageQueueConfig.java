package io.realmarket.propeler.config.blockchain_message_queue;

import io.realmarket.propeler.service.blockchain.queue.BlockchainQueueMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Configuration
public class BlockchainMessageQueueConfig {

  private final BlockingQueue<BlockchainQueueMessage> blockchainMessageQueue;

  public BlockchainMessageQueueConfig() {
    this.blockchainMessageQueue = new LinkedBlockingDeque<>();
  }

  @Bean
  @Scope(
      scopeName = WebApplicationContext.SCOPE_APPLICATION,
      proxyMode = ScopedProxyMode.TARGET_CLASS)
  public BlockingQueue<BlockchainQueueMessage> blockchainMessageQueue() {
    return blockchainMessageQueue;
  }
}
