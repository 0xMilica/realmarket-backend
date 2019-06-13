package io.realmarket.propeler.service.impl.blockchain.impl;

import io.realmarket.propeler.service.blockchain.BlockchainMethod;
import io.realmarket.propeler.service.blockchain.exception.BlockchainException;
import io.realmarket.propeler.service.blockchain.impl.BlockchainCommunicationServiceImpl;
import io.realmarket.propeler.util.BlockchainUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static io.realmarket.propeler.util.BlockchainUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BlockchainCommunicationServiceImpl.class)
public class BlockchainCommunicationServiceImplTest {

  @Mock RestTemplate restTemplate;

  @InjectMocks BlockchainCommunicationServiceImpl blockchainCommunicationService;

  @Test
  public void Invoke_Should_Return_BlockchainResponse() {
    ReflectionTestUtils.setField(blockchainCommunicationService, "active", true);

    when(restTemplate.exchange(anyString(), any(), any(), any(Class.class)))
        .thenReturn(TEST_RESPONSE_OK);
    when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(getMapMocked(true));

    Map<String, Object> retVal =
        blockchainCommunicationService.invoke(
            BlockchainMethod.USER_REGISTRATION, BlockchainUtils.TEST_REGISTRATION_DTO);

    assertEquals(TEST_MESSAGE, retVal.get("message"));
  }

  @Test
  public void Invoke_Should_Return_Null() {
    ReflectionTestUtils.setField(blockchainCommunicationService, "active", false);

    Map<String, Object> retVal =
        blockchainCommunicationService.invoke(
            BlockchainMethod.USER_REGISTRATION, BlockchainUtils.TEST_REGISTRATION_DTO);

    assertNull(retVal);
  }

  @Test(expected = BlockchainException.class)
  public void Invoke_Should_Return_Throw_BlockchainException_WhenReponseNull() {
    ReflectionTestUtils.setField(blockchainCommunicationService, "active", true);

    when(restTemplate.exchange(anyString(), any(), any(), any(Class.class)))
        .thenReturn(TEST_RESPONSE_OK);
    when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(getMapMocked(false));

    blockchainCommunicationService.invoke(
        BlockchainMethod.USER_REGISTRATION, BlockchainUtils.TEST_REGISTRATION_DTO);
  }

  @Test(expected = BlockchainException.class)
  public void Invoke_Should_Return_Throw_BlockchainException_WhenReponseError() {
    ReflectionTestUtils.setField(blockchainCommunicationService, "active", true);

    when(restTemplate.exchange(anyString(), any(), any(), any(Class.class)))
        .thenReturn(TEST_RESPONSE_OK);

    blockchainCommunicationService.invoke(
        BlockchainMethod.USER_REGISTRATION, BlockchainUtils.TEST_REGISTRATION_DTO);
  }

  @Test(expected = BlockchainException.class)
  public void Invoke_Should_Return_Throw_BlockchainException_WhenEnrollmentFailed() {
    ReflectionTestUtils.setField(blockchainCommunicationService, "active", true);

    when(restTemplate.exchange(anyString(), any(), any(), any(Class.class)))
        .thenReturn(TEST_RESPONSE_ERROR);

    blockchainCommunicationService.invoke(
        BlockchainMethod.USER_REGISTRATION, BlockchainUtils.TEST_REGISTRATION_DTO);
  }
}
