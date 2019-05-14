package io.realmarket.propeler.model.enums;

public enum ECampaignDocumentType {
  DOCTYPE_APR_PAPER("DOCTYPE_APR_PAPER"),
  DOCTYPE_BUSINESS_PLAN("DOCTYPE_BUSINESS_PLAN"),
  DOCTYPE_PITCH_DECK("DOCTYPE_PITCH_DECK"),
  DOCTYPE_DUE_DILIGENCE_OTHER("DOCTYPE_DUE_DILIGENCE_OTHER");

  private final String text;

  ECampaignDocumentType(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
