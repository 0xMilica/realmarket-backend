package io.realmarket.propeler.model.enums;

public enum EDocumentAccessLevel {
  PLATFORM_ADMINS("PLATFORM_ADMINS"),
  INVESTORS("INVESTORS"),
  PUBLIC("PUBLIC");

  private final String text;

  EDocumentAccessLevel(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
