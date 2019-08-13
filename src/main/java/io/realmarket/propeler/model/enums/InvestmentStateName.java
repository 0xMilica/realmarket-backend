package io.realmarket.propeler.model.enums;

public enum InvestmentStateName {
  INITIAL("INITIAL"),
  OWNER_APPROVED("OWNER_APPROVED"),
  OWNER_REJECTED("OWNER_REJECTED"),
  PAID("PAID"),
  EXPIRED("EXPIRED"),
  REVOKED("REVOKED"),
  AUDIT_APPROVED("AUDIT_APPROVED"),
  AUDIT_REJECTED("AUDIT_REJECTED");

  private final String text;

  InvestmentStateName(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
