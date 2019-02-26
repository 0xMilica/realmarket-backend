package io.realmarket.propeler.api.dto.enums;

public enum E2FAStatus {
  INITIALIZE("INITIALIZE"),
  VALIDATE("VALIDATE"),
  REMEMBER_ME("REMEMBER_ME");

  private final String text;

  E2FAStatus(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
