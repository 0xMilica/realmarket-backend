package io.realmarket.propeler.model.enums;

public enum CampaignDocumentTypeName {
  DOCTYPE_DUE_DILIGENCE("DOCTYPE_DUE_DILIGENCE"),
  DOCTYPE_LEGAL("DOCTYPE_LEGAL");

  private final String text;

  CampaignDocumentTypeName(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
