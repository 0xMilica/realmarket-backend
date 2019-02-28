package io.realmarket.propeler.model.enums;

public enum ECampaignTopicType {
  PROBLEM("PROBLEM"),
  SOLUTION("SOLUTION"),
  RISK_AND_COMPETITION("RISK_AND_COMPETITION"),
  CAPITAL_AND_EXIT("CAPITAL_AND_EXIT"),
  UPDATES("UPDATES"),
  OVERVIEW("OVERVIEW"),
  ;

  private final String text;

  ECampaignTopicType(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
