package io.realmarket.propeler.service.exception;

public class ForbiddenRoleException extends RuntimeException {

  public ForbiddenRoleException(String message) {
    super(message);
  }
}
