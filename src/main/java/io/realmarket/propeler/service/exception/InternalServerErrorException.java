package io.realmarket.propeler.service.exception;

public class InternalServerErrorException extends RuntimeException {

  public InternalServerErrorException(String message) {
    super(message);
  }
}
