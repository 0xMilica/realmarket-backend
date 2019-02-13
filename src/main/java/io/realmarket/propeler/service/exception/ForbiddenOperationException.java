package io.realmarket.propeler.service.exception;

public class ForbiddenOperationException extends RuntimeException {

  public ForbiddenOperationException(String message) {
    super(message);
  }
}
