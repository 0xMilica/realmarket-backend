package io.realmarket.propeler.service.blockchain.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.realmarket.propeler.service.blockchain.BlockchainCommunicationService;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import io.realmarket.propeler.service.blockchain.dto.EmailChangeDto;
import io.realmarket.propeler.service.blockchain.dto.PasswordChangeDto;
import io.realmarket.propeler.service.blockchain.dto.RegistrationDto;
import io.realmarket.propeler.service.blockchain.exception.BlockchainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class BlockchainCommunicationServiceImpl implements BlockchainCommunicationService {
  @Value("${blockchain.active}")
  private Boolean active;

  @Value("${blockchain.chaincode_name}")
  private String chaincodeName;

  @Value("${blockchain.channel_name}")
  private String channelName;

  @Value("${blockchain.address}")
  private String blockchainAddress;

  @Value("${blockchain.peers}")
  private String[] peersAddresses;

  @Value("${blockchain.method}")
  private String method;

  @Value("${blockchain.arguments}")
  private String arguments;

  @Value("${blockchain.user.organization}")
  private String organization;

  @Value("${blockchain.user.username}")
  private String username;

  private RestTemplate restTemplate;

  private static final String RESPONSE_MESSAGE = "message";

  public BlockchainCommunicationServiceImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public Map<String, Object> invoke(String methodName, AbstractBlockchainDto dto)
      throws JsonProcessingException {
    if (!active) {
      log.info("Blockchain is off.");
      return null;
    }

    log.info("Invoke chaincode method: {}", methodName);

    String invocationUrl =
        String.format(
            "%s/channels/%s/chaincodes/%s", blockchainAddress, channelName, chaincodeName);

    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + enrollUser());

    Map<String, Object> args = new HashMap<>();
    args.put("peers", peersAddresses);
    args.put(method, methodName);
    args.put(arguments, Collections.singletonList(new ObjectMapper().writeValueAsString(dto)));

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(args, headers);
    Map<String, Object> response = restTemplate.postForObject(invocationUrl, entity, Map.class);

    if (response == null) {
      log.error("Failed to invoke chaincode. Response object is null.");
      throw new BlockchainException("Response is null.");
    } else if (!(Boolean) response.get("success")) {
      log.error("Failed to invoke chaincode. Error: {}", response.get(RESPONSE_MESSAGE));
      throw new BlockchainException((String) response.get(RESPONSE_MESSAGE));
    }

    log.debug("Invocation response: {}", response.get(RESPONSE_MESSAGE));

    return response;
  }

  public String enrollUser() {
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

  @PostConstruct
  public void test() {
    try {
      invoke(
          "UserRegistration",
          RegistrationDto.builder()
              .userId(1L)
              .timestamp(Instant.now().getEpochSecond())
              .IP("127.0.0.1")
              .role("ROLE")
              .build());
      invoke(
          "UserPasswordChange",
          PasswordChangeDto.builder()
              .userId(1L)
              .timestamp(Instant.now().getEpochSecond())
              .IP("127.0.0.1")
              .build());
      invoke(
          "UserEmailChange",
          EmailChangeDto.builder()
              .userId(1L)
              .timestamp(Instant.now().getEpochSecond())
              .IP("127.0.0.1")
              .newEmailHash("tralala")
              .build());
      // invoke("UserRegenerationOfRecovery", REGENERATION_OF_RECOVERY_DTO);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}
