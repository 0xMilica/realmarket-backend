package io.realmarket.propeler.model.enums;

public enum UserRoleName {
  ROLE_ADMIN("ROLE_ADMIN"),
  ROLE_INVESTOR("ROLE_INVESTOR"),
  ROLE_ENTREPRENEUR("ROLE_ENTREPRENEUR"),
  ROLE_AUDITOR("ROLE_AUDITOR");

  private final String text;

  UserRoleName(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
