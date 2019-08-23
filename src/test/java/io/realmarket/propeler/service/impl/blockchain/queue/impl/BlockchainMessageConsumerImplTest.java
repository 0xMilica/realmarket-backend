package io.realmarket.propeler.service.impl.blockchain.queue.impl;

import io.realmarket.propeler.service.blockchain.BlockchainMethod;
import io.realmarket.propeler.service.blockchain.exception.BlockchainException;
import io.realmarket.propeler.service.blockchain.queue.BlockchainQueueMessage;
import io.realmarket.propeler.service.blockchain.queue.impl.BlockchainMessageConsumerImpl;
import io.realmarket.propeler.service.blockchain.queue.impl.BlockchainMessageProducerImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.BlockingQueue;

import static io.realmarket.propeler.util.BlockchainUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BlockchainMessageProducerImpl.class)
public class BlockchainMessageConsumerImplTest {

  @Mock RestTemplate restTemplate;

  @Mock BlockingQueue<BlockchainQueueMessage> blockchainMessageQueue;

  @InjectMocks BlockchainMessageConsumerImpl blockchainMessageConsumer;

  @Test
  public void ConsumeMessage_Should_ThrowException() throws Exception {
    when(restTemplate.postForObject(anyString(), any(), any(), any(Class.class)))
        .thenReturn(getMapMocked(true));

    when(restTemplate.exchange(anyString(), any(), any(), any(Class.class)))
        .thenReturn(TEST_RESPONSE_OK);

    blockchainMessageConsumer.processMessage(
        new BlockchainQueueMessage(
            BlockchainMethod.USER_REGISTRATION, TEST_REGISTRATION_DTO, TEST_IP));
  }

  @Test(expected = BlockchainException.class)
  public void Invoke_Should_Return_Throw_BlockchainException_WhenResponseNull() {
    when(restTemplate.exchange(anyString(), any(), any(), any(Class.class)))
        .thenReturn(TEST_RESPONSE_OK);
    when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(getMapMocked(false));

    blockchainMessageConsumer.processMessage(
        new BlockchainQueueMessage(
            BlockchainMethod.USER_REGISTRATION, TEST_REGISTRATION_DTO, TEST_IP));
  }

  @Test(expected = BlockchainException.class)
  public void Invoke_Should_Return_Throw_BlockchainException_WhenResponseError() {
    when(restTemplate.exchange(anyString(), any(), any(), any(Class.class)))
        .thenReturn(TEST_RESPONSE_OK);

    blockchainMessageConsumer.processMessage(
        new BlockchainQueueMessage(
            BlockchainMethod.USER_REGISTRATION, TEST_REGISTRATION_DTO, TEST_IP));
  }

  @Test(expected = BlockchainException.class)
  public void Invoke_Should_Return_Throw_BlockchainException_WhenEnrollmentFailed() {
    when(restTemplate.exchange(anyString(), any(), any(), any(Class.class)))
        .thenReturn(TEST_RESPONSE_ERROR);

    blockchainMessageConsumer.processMessage(
        new BlockchainQueueMessage(
            BlockchainMethod.USER_REGISTRATION, TEST_REGISTRATION_DTO, TEST_IP));
  }
}
