package io.realmarket.propeler.model.enums;

public enum EUserRole {
  ROLE_ADMIN("ROLE_ADMIN"),
  ROLE_INVESTOR("ROLE_INVESTOR"),
  ROLE_ENTREPRENEUR("ROLE_ENTREPRENEUR");

  private final String text;

  EUserRole(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
