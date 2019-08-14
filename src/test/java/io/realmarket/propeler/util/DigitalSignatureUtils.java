package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.DigitalSignaturePrivateDto;
import io.realmarket.propeler.api.dto.DigitalSignaturePublicDto;
import io.realmarket.propeler.model.DigitalSignature;

public class DigitalSignatureUtils {
  public static final String TEST_ENCRYPTED_PRIVATE_KEY = "TEST_ENCRYPTED_PRIVATE_KEY";
  public static final String TEST_PUBLIC_KEY = "TEST_PUBLIC_KEY";
  public static final String TEST_INITIAL_VECTOR = "TEST_INITIAL_VECTOR";
  public static final String TEST_SALT = "TEST_SALT";
  public static final Integer TEST_PASS_LENGTH = 6;

  public static final DigitalSignature TEST_DIGITAL_SIGNATURE =
      DigitalSignature.builder()
          .id(1L)
          .encryptedPrivateKey(TEST_ENCRYPTED_PRIVATE_KEY)
          .publicKey(TEST_PUBLIC_KEY)
          .initialVector(TEST_INITIAL_VECTOR)
          .salt(TEST_SALT)
          .passLength(TEST_PASS_LENGTH)
          .auth(AuthUtils.TEST_AUTH)
          .build();

  public static final DigitalSignaturePrivateDto TEST_DIGITAL_SIGNATURE_PRIVATE_DTO =
      DigitalSignaturePrivateDto.builder()
          .encryptedPrivateKey(TEST_ENCRYPTED_PRIVATE_KEY)
          .publicKey(TEST_PUBLIC_KEY)
          .initialVector(TEST_INITIAL_VECTOR)
          .salt(TEST_SALT)
          .passLength(TEST_PASS_LENGTH)
          .build();

  public static final DigitalSignaturePublicDto TEST_DIGITAL_SIGNATURE_PUBLIC_DTO =
      DigitalSignaturePublicDto.builder()
          .id(1L)
          .publicKey(TEST_PUBLIC_KEY)
          .ownerId(AuthUtils.TEST_AUTH_ID)
          .build();
}
