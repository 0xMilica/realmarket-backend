package io.realmarket.propeler.model.enums;

public enum TemporaryTokenTypeName {
  SETUP_2FA_TOKEN("SETUP_2FA_TOKEN"),
  LOGIN_TOKEN("LOGIN_TOKEN"),
  REGISTRATION_TOKEN("REGISTRATION_TOKEN"),
  EMAIL_TOKEN("EMAIL_TOKEN"),
  RESET_PASSWORD_TOKEN("RESET_PASSWORD_TOKEN"),
  EMAIL_CHANGE_TOKEN("EMAIL_CHANGE_TOKEN"),
  PASSWORD_VERIFIED_TOKEN("PASSWORD_VERIFIED_TOKEN");

  private final String text;

  TemporaryTokenTypeName(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
