package io.realmarket.propeler.model.enums;

public enum ECampaignDocumentType {
  DOCTYPE_DUE_DILIGENCE("DOCTYPE_DUE_DILIGENCE"),
  DOCTYPE_LEGAL("DOCTYPE_LEGAL");

  private final String text;

  ECampaignDocumentType(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
