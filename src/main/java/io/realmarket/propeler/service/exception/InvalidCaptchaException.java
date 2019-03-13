package io.realmarket.propeler.service.exception;

public class InvalidCaptchaException extends RuntimeException {

  public InvalidCaptchaException(String message) {
    super(message);
  }
}
