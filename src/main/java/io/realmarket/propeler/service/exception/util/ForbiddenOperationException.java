package io.realmarket.propeler.service.exception.util;

public class ForbiddenOperationException extends RuntimeException {

  public ForbiddenOperationException(String message) {
    super(message);
  }
}
