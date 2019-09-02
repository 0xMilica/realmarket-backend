package io.realmarket.propeler.e2e.util;

import io.realmarket.propeler.api.dto.DigitalSignaturePrivateDto;

public class DigitalSignatureUtils {

  private DigitalSignatureUtils() {}

  public static final String TEST_ENCRYPTED_PRIVATE_KEY = "TEST_ENCRYPTED_PRIVATE_KEY";
  public static final String TEST_PUBLIC_KEY = "TEST_PUBLIC_KEY";
  public static final String TEST_INITIAL_VECTOR = "TEST_INITIAL_VECTOR";
  public static final String TEST_SALT = "TEST_SALT";
  public static final Integer TEST_PASS_LENGTH = 6;
  public static final Long TEST_AUTH_ID = 5L;
  public static final Long TEST_AUTH_ID2 = 6L;

  public static final DigitalSignaturePrivateDto TEST_DIGITAL_SIGNATURE_PRIVATE_DTO =
      DigitalSignaturePrivateDto.builder()
          .encryptedPrivateKey(TEST_ENCRYPTED_PRIVATE_KEY)
          .publicKey(TEST_PUBLIC_KEY)
          .initialVector(TEST_INITIAL_VECTOR)
          .salt(TEST_SALT)
          .passLength(TEST_PASS_LENGTH)
          .build();
}
