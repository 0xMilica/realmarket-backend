package io.realmarket.propeler.model.enums;

public enum NotificationType {
  ACCEPT_CAMPAIGN("ACCEPT_CAMPAIGN"),
  REJECT_CAMPAIGN("REJECT_CAMPAIGN"),
  KYC_APPROVAL("KYC_APPROVAL"),
  KYC_REJECTION("KYC_REJECTION");

  private final String text;

  NotificationType(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
