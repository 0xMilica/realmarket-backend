package io.realmarket.propeler.util;

import io.realmarket.propeler.service.blockchain.dto.*;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static io.realmarket.propeler.util.AuthUtils.TEST_EMAIL;
import static io.realmarket.propeler.util.AuthUtils.TEST_USERNAME;

public class BlockchainUtils {

  public static final String TEST_IP = "127.0.0.1";
  public static final Long TEST_ID = 123L;
  public static final String TEST_ROLE = "Role";
  public static final String TEST_MESSAGE = "message";

  public static final RegistrationDto TEST_REGISTRATION_DTO =
      RegistrationDto.builder()
          .userId(TEST_ID)
          .timestamp(Instant.now().getEpochSecond())
          .IP(TEST_IP)
          .role(TEST_ROLE)
          .username(TEST_USERNAME)
          .person(HashedPersonDetails.builder().email(TEST_EMAIL).build())
          .build();

  public static final PasswordChangeDto TEST_PASSWORD_CHANGE_DTO =
      PasswordChangeDto.builder()
          .userId(TEST_ID)
          .timestamp(Instant.now().getEpochSecond())
          .IP(TEST_IP)
          .build();

  public static final EmailChangeDto TEST_EMAIL_CHANGE_DTO =
      EmailChangeDto.builder()
          .userId(TEST_ID)
          .timestamp(Instant.now().getEpochSecond())
          .IP(TEST_IP)
          .newEmailHash(TEST_EMAIL)
          .build();

  public static final RegenerationOfRecoveryDto TEST_REGENERATION_OF_RECOVERY =
      RegenerationOfRecoveryDto.builder()
          .userId(TEST_ID)
          .timestamp(Instant.now().getEpochSecond())
          .IP(TEST_IP)
          .build();

  public static final Map<String, Object> getMapMocked(Boolean success) {
    Map<String, Object> m = new HashMap<>();
    m.put("success", success);
    m.put("message", TEST_MESSAGE);
    return m;
  }

  public static final ResponseEntity<Map> TEST_RESPONSE_OK = ResponseEntity.ok(getMapMocked(true));
  public static final ResponseEntity<Map> TEST_RESPONSE_ERROR =
      ResponseEntity.ok(getMapMocked(false));
}
