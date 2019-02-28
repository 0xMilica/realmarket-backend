package io.realmarket.propeler.model.enums;

public enum ECampaignDocumentAccessLevel {
  PLATFORM_ADMINS("PLATFORM_ADMINS"),
  INVESTORS("INVESTORS"),
  PUBLIC("PUBLIC");

  private final String text;

  ECampaignDocumentAccessLevel(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
