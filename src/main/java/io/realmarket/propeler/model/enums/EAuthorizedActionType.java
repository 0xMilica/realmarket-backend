package io.realmarket.propeler.model.enums;

public enum EAuthorizedActionType {
  NEW_TOTP_SECRET("NEW_TOTP_SECRET"),
  NEW_EMAIL("NEW_EMAIL"),
  NEW_PASSWORD("NEW_PASSWORD");

  private final String text;

  EAuthorizedActionType(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
