package io.realmarket.propeler.service.exception;

public class PayPalOrderAlreadyCapturedException extends RuntimeException {
  public PayPalOrderAlreadyCapturedException(String message) {
    super(message);
  }
}
