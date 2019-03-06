package io.realmarket.propeler.service.exception;

public class CampaignNameAlreadyExistsException extends RuntimeException {

  public CampaignNameAlreadyExistsException(String message) {
    super(message);
  }
}
