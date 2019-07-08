package io.realmarket.propeler.model.enums;

public enum RequestStateName {
  PENDING("PENDING"),
  APPROVED("APPROVED"),
  DECLINED("DECLINED");

  private final String text;

  RequestStateName(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
