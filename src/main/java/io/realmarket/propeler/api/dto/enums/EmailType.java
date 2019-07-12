package io.realmarket.propeler.api.dto.enums;

public enum EmailType {
  REGISTER("REGISTER"),
  RESET_PASSWORD("RESET_PASSWORD"),
  RECOVER_USERNAME("RECOVER_USERNAME"),
  CHANGE_EMAIL("CHANGE_EMAIL"),
  SECRET_CHANGE("SECRET_CHANGE"),
  ACCOUNT_BLOCKED("ACCOUNT_BLOCKED"),
  NEW_CAMPAIGN_OPPORTUNITY("NEW_CAMPAIGN_OPPORTUNITY"),
  NEW_CAMPAIGN_OPPORTUNITIES("NEW_CAMPAIGN_OPPORTUNITIES"),
  FUNDRAISING_PROPOSAL("FUNDRAISING_PROPOSAL"),
  FUNDRAISING_PROPOSAL_REJECTION("FUNDRAISING_PROPOSAL_REJECTION");


  private final String text;

  EmailType(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
