package io.realmarket.propeler.model.enums;

public enum AuthStateName {
  CONFIRM_REGISTRATION("CONFIRM_REGISTRATION"),
  INITIALIZE_2FA("INITIALIZE_2FA"),
  ACTIVE("ACTIVE");

  private final String text;

  AuthStateName(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
