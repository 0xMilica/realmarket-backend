package io.realmarket.propeler.service.util;

import lombok.extern.slf4j.Slf4j;
import org.jboss.aerogear.security.otp.api.Base32;

import java.security.SecureRandom;

@Slf4j
public class RandomStringBuilder {
  private static final Double SCALING_OF_BASE36 = 1.6;

  private static final SecureRandom numberGenerator = new SecureRandom();

  public static String generateBase32String(int length) {
    /*try {
      random = SecureRandom.getInstanceStrong();
    } catch (NoSuchAlgorithmException e) {
      log.error("Algorithm not present:" + e.getMessage());
      throw new InternalServerErrorException(ExceptionMessages.COULD_NOT_GENERATE_RANDOM_STRING);
    }*/
    byte[] values = new byte[(int) (length / SCALING_OF_BASE36)];
    numberGenerator.nextBytes(values);
    return Base32.encode(values);
  }

  private RandomStringBuilder(){}
}
