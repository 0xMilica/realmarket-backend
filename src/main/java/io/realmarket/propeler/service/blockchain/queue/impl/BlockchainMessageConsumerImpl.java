package io.realmarket.propeler.service.blockchain.queue.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.realmarket.propeler.service.blockchain.exception.BlockchainException;
import io.realmarket.propeler.service.blockchain.queue.BlockchainQueueMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class BlockchainMessageConsumerImpl {

  private static final String RESPONSE_MESSAGE = "message";

  private RestTemplate restTemplate;

  @Value("${blockchain.chaincode_name}")
  private String chaincodeName;

  @Value("${blockchain.channel_name}")
  private String channelName;

  @Value("${blockchain.address}")
  private String blockchainAddress;

  @Value("${blockchain.user.organization}")
  private String organization;

  @Value("${blockchain.invocation.peers}")
  private String[] peersAddresses;

  @Value("${blockchain.invocation.method}")
  private String method;

  @Value("${blockchain.invocation.arguments}")
  private String arguments;

  @Autowired
  public BlockchainMessageConsumerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Retryable(maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(multiplier = 2))
  public void processMessage(BlockchainQueueMessage message) {
    HttpEntity<Map<String, Object>> entity = generateEntity(message);

    String invocationUrl =
        String.format(
            "%s/channels/%s/chaincodes/%s", blockchainAddress, channelName, chaincodeName);
    Map<String, Object> response = restTemplate.postForObject(invocationUrl, entity, Map.class);
    processResponse(response);
  }

  private String enrollUser(String username) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

    LinkedMultiValueMap<String, Object> args = new LinkedMultiValueMap<>();
    args.add("username", username);
    args.add("orgName", organization);

    HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(args, headers);
    ResponseEntity<Map> response =
        restTemplate.exchange(blockchainAddress + "/users", HttpMethod.POST, entity, Map.class);

    if (response.getBody() != null && !(Boolean) response.getBody().get("success")) {
      log.error(
          "Failed to enroll user on blockchain network. Error: {}",
          response.getBody().get(RESPONSE_MESSAGE));
      throw new BlockchainException((String) response.getBody().get(RESPONSE_MESSAGE));
    }

    log.debug("Enroll response  : " + response.getBody());
    return (String) response.getBody().get("token");
  }

  private void processResponse(Map<String, Object> response) {
    if (response == null) {
      log.error("Failed to invoke chaincode. Response object is null.");
      throw new BlockchainException("Response is null.");

    } else if (Boolean.FALSE.equals(response.get("success"))) {
      log.error("Failed to produceMessage chaincode. Error: {}", response.get(RESPONSE_MESSAGE));
      throw new BlockchainException((String) response.get(RESPONSE_MESSAGE));

    } else {
      log.info("Invocation response: {}", response.get(RESPONSE_MESSAGE));
    }
  }

  private HttpEntity<Map<String, Object>> generateEntity(BlockchainQueueMessage message) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + enrollUser(message.getUsername()));

    Map<String, Object> args = new HashMap<>();
    args.put("peers", peersAddresses);
    args.put(method, message.getMethodName());

    try {

      log.debug("Blockchain dto: {}", message.getDto());
      args.put(
          arguments,
          Collections.singletonList(new ObjectMapper().writeValueAsString(message.getDto())));
    } catch (JsonProcessingException jpe) {
      log.error(jpe.getMessage());
      throw new BlockchainException("Blockchain DTO serialization error: " + jpe.getMessage());
    }

    return new HttpEntity<>(args, headers);
  }
}
