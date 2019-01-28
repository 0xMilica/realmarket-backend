package io.realmarket.propeler.service.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

  public UsernameAlreadyExistsException(String message) {
    super(message);
  }
}
