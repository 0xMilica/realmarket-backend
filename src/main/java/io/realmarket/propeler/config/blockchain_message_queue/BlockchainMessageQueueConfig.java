package io.realmarket.propeler.config.blockchain_message_queue;

import io.realmarket.propeler.model.BlockchainMessage;
import io.realmarket.propeler.service.BlockchainMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class BlockchainMessageQueueConfig {

  private final BlockingQueue<BlockchainMessage> blockchainMessageQueue;

  @Autowired
  public BlockchainMessageQueueConfig(BlockchainMessageService blockchainMessageService) {
    blockchainMessageQueue =
        new LinkedBlockingQueue<>(blockchainMessageService.findAllOrderedById());
  }

  @Bean
  @Scope(
      scopeName = WebApplicationContext.SCOPE_APPLICATION,
      proxyMode = ScopedProxyMode.TARGET_CLASS)
  public BlockingQueue<BlockchainMessage> blockchainMessageQueue() {
    return blockchainMessageQueue;
  }
}
