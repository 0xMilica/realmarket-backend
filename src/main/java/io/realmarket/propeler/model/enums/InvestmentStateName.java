package io.realmarket.propeler.model.enums;

public enum InvestmentStateName {
  PENDING("PENDING"),
  APPROVED("APPROVED"),
  DECLINED("DECLINED"),
  CANCELLED("CANCELLED");

  private final String text;

  InvestmentStateName(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
