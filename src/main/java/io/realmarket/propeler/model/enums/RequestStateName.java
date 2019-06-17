package io.realmarket.propeler.model.enums;

public enum RequestStateName {
  PENDING("PENDING"),
  APPROVED("APPROVED"),
  DECLINED("DECLINED");

  private final String text;

  RequestStateName(final String text) {
    this.text = text;
  }

  public static RequestStateName fromString(String text) {
    for (RequestStateName requestStateName : RequestStateName.values()) {
      if (requestStateName.text.equalsIgnoreCase(text)) {
        return requestStateName;
      }
    }
    return RequestStateName.PENDING;
  }

  @Override
  public String toString() {
    return text;
  }
}
