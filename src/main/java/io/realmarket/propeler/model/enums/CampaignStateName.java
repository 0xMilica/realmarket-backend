package io.realmarket.propeler.model.enums;

public enum CampaignStateName {
  INITIAL("INITIAL"),
  REVIEW_READY("REVIEW_READY"),
  AUDIT("AUDIT"),
  LAUNCH_READY("LAUNCH_READY"),
  ACTIVE("ACTIVE"),
  SUCCESSFUL("SUCCESSFUL"),
  UNSUCCESSFUL("UNSUCCESSFUL"),
  DELETED("DELETED");

  private final String text;

  CampaignStateName(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
