package io.realmarket.propeler.service.exception;

public class EmailSendingException extends RuntimeException {

  public EmailSendingException(String message) {
    super(message);
  }
}
