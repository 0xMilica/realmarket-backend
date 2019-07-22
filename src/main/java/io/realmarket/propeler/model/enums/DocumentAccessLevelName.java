package io.realmarket.propeler.model.enums;

public enum DocumentAccessLevelName {
  PUBLIC("PUBLIC"),
  PRIVATE("PRIVATE"),
  ON_DEMAND("ON_DEMAND"),
  INVESTORS("INVESTORS"),
  PLATFORM_ADMINS("PLATFORM_ADMINS");

  private final String text;

  DocumentAccessLevelName(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
