package io.realmarket.propeler.model.enums;

public enum InvestmentStateName {
  INITIAL("INITIAL"),
  PAID("PAID"),
  REVOKED("REVOKED"),
  APPROVED("APPROVED"),
  REJECTED("REJECTED");

  private final String text;

  InvestmentStateName(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
