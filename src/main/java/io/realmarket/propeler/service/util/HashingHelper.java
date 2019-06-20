package io.realmarket.propeler.service.util;

import io.realmarket.propeler.service.exception.HashException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingHelper {

  private HashingHelper() {}

  public static final String HASH_ALGORITHM = "SHA-256";
  private static MessageDigest messageDigest;

  static {
    try {
      messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
    } catch (NoSuchAlgorithmException e) {
      throw new HashException(ExceptionMessages.NO_HASH_ALGORITHM);
    }
  }

  public static String hash(String s) {
    if (s == null) return null;
    messageDigest.update(s.getBytes());
    return new String(messageDigest.digest());
  }
}
