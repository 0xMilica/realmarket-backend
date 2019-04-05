package io.realmarket.propeler.service.exception;

import io.realmarket.propeler.service.exception.util.ExceptionMessages;

public class ActiveCampaignAlreadyExistsException extends RuntimeException {

  public ActiveCampaignAlreadyExistsException() {
    super(ExceptionMessages.ACTIVE_CAMPAIGN_EXISTS);
  }
}
