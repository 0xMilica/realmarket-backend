package io.realmarket.propeler.service.util;

import io.realmarket.propeler.service.exception.InternalServerErrorException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;
import org.jboss.aerogear.security.otp.api.Base32;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Slf4j
public class RandomStringBuilder {
  private static final String VALID_SYMBOLS =
      "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm01234567890-_";

  private static final Double SCALING_OF_BASE36 = 1.6;

  public static String generateBase32String(int length) {
    SecureRandom random;
    try {
      random = SecureRandom.getInstanceStrong();
    } catch (NoSuchAlgorithmException e) {
      log.error("Algorithm not present:" + e.getMessage());
      throw new InternalServerErrorException(ExceptionMessages.COULD_NOT_GENERATE_RANDOM_STRING);
    }
    byte[] values =
            new byte[(int) (length / SCALING_OF_BASE36)];
    random.nextBytes(values);
    return Base32.encode(values);
  }

  public static String generateToken(int length) {

    SecureRandom random;
    StringBuilder stringBuilder = new StringBuilder();

    try {
      random = SecureRandom.getInstanceStrong();
    } catch (NoSuchAlgorithmException e) {
      log.error("No secure algorithm found!");
      throw new InternalServerErrorException(ExceptionMessages.COULD_NOT_GENERATE_TOKEN);
    }

    for (int i = 0; i < length; i++) {
      stringBuilder.append(VALID_SYMBOLS.charAt(random.nextInt(VALID_SYMBOLS.length())));
    }
    return stringBuilder.toString();
  }
}
