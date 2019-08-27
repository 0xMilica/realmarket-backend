package io.realmarket.propeler.model.enums;

public enum NotificationType {
  ACCEPT_CAMPAIGN("ACCEPT_CAMPAIGN"),
  REJECT_CAMPAIGN("REJECT_CAMPAIGN"),
  KYC_APPROVAL("KYC_APPROVAL"),
  KYC_REJECTION("KYC_REJECTION"),
  ACCEPT_INVESTOR("ACCEPT_INVESTOR"),
  REJECT_INVESTOR("REJECT_INVESTOR"),
  ACCEPT_DOCUMENTS("ACCEPT_DOCUMENTS"),
  REJECT_DOCUMENTS("REJECT_DOCUMENTS");

  private final String text;

  NotificationType(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
