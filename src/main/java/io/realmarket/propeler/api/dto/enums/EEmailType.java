package io.realmarket.propeler.api.dto.enums;

public enum EEmailType {
  REGISTER("REGISTER"),
  RESET_PASSWORD("RESET_PASSWORD"),
  RECOVER_USERNAME("RECOVER_USERNAME");

  private final String text;

  EEmailType(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
