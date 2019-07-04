package io.realmarket.propeler.model.enums;

public enum CampaignStateName {
  INITIAL("INITIAL"),
  REVIEW_READY("REVIEW_READY"),
  AUDIT("AUDIT"),
  LAUNCH_READY("LAUNCH_READY"),
  ACTIVE("ACTIVE"),
  POST_CAMPAIGN("POST_CAMPAIGN"),
  DELETED("DELETED");

  private final String text;

  CampaignStateName(final String text) {
    this.text = text;
  }

  public static CampaignStateName fromString(String text) {
    for (CampaignStateName campaignStateName : CampaignStateName.values()) {
      if (campaignStateName.text.equalsIgnoreCase(text)) {
        return campaignStateName;
      }
    }
    return CampaignStateName.INITIAL;
  }

  @Override
  public String toString() {
    return text;
  }
}
