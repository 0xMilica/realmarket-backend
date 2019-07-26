package io.realmarket.propeler.service.impl.blockchain.impl;

import io.realmarket.propeler.service.blockchain.BlockchainMethod;
import io.realmarket.propeler.service.blockchain.dto.user.HashedPersonDetails;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static io.realmarket.propeler.util.BlockchainUtils.*;
import static io.realmarket.propeler.util.PersonUtils.TEST_PERSON;
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
  public void Invoke_Should_Return_BlockchainResponse()
      throws ExecutionException, InterruptedException {
    ReflectionTestUtils.setField(blockchainCommunicationService, "active", true);

    when(restTemplate.exchange(anyString(), any(), any(), any(Class.class)))
        .thenReturn(TEST_RESPONSE_OK);
    when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(getMapMocked(true));

    TEST_REGISTRATION_DTO.setPerson(new HashedPersonDetails(TEST_PERSON));
    Future<Map<String, Object>> retVal =
        blockchainCommunicationService.invoke(
            BlockchainMethod.USER_REGISTRATION,
            BlockchainUtils.TEST_REGISTRATION_DTO,
            TEST_USERNAME,
            TEST_IP);

    assertEquals(TEST_MESSAGE, retVal.get().get("message"));
  }

  @Test
  public void Invoke_Should_Return_Null() {
    ReflectionTestUtils.setField(blockchainCommunicationService, "active", false);

    Future<Map<String, Object>> retVal =
        blockchainCommunicationService.invoke(
            BlockchainMethod.USER_REGISTRATION, TEST_REGISTRATION_DTO, TEST_USERNAME, TEST_IP);

    assertNull(retVal);
  }

  @Test(expected = BlockchainException.class)
  public void Invoke_Should_Return_Throw_BlockchainException_WhenResponseNull() {
    ReflectionTestUtils.setField(blockchainCommunicationService, "active", true);

    when(restTemplate.exchange(anyString(), any(), any(), any(Class.class)))
        .thenReturn(TEST_RESPONSE_OK);
    when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(getMapMocked(false));

    blockchainCommunicationService.invoke(
        BlockchainMethod.USER_REGISTRATION, TEST_REGISTRATION_DTO, TEST_USERNAME, TEST_IP);
  }

  @Test(expected = BlockchainException.class)
  public void Invoke_Should_Return_Throw_BlockchainException_WhenResponseError() {
    ReflectionTestUtils.setField(blockchainCommunicationService, "active", true);

    when(restTemplate.exchange(anyString(), any(), any(), any(Class.class)))
        .thenReturn(TEST_RESPONSE_OK);

    blockchainCommunicationService.invoke(
        BlockchainMethod.USER_REGISTRATION, TEST_REGISTRATION_DTO, TEST_USERNAME, null);
  }

  @Test(expected = BlockchainException.class)
  public void Invoke_Should_Return_Throw_BlockchainException_WhenEnrollmentFailed() {
    ReflectionTestUtils.setField(blockchainCommunicationService, "active", true);

    when(restTemplate.exchange(anyString(), any(), any(), any(Class.class)))
        .thenReturn(TEST_RESPONSE_ERROR);

    blockchainCommunicationService.invoke(
        BlockchainMethod.USER_REGISTRATION, TEST_REGISTRATION_DTO, TEST_USERNAME, TEST_IP);
  }
}
