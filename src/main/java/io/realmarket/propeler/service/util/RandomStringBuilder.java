package io.realmarket.propeler.service.util;

import io.realmarket.propeler.service.exception.InternalServerErrorException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Slf4j
public class RandomStringBuilder {
  private static final String VALID_SYMBOLS =
      "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm01234567890-_";

  public static String generateToken(int length) {
    log.info("BIGB - generateToken temp token");

    SecureRandom random;
    StringBuilder stringBuilder = new StringBuilder();

    try {
      random = SecureRandom.getInstanceStrong();
      log.info("BIGB - random -  " + random);

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
