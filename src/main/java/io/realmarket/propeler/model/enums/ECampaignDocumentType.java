package io.realmarket.propeler.model.enums;

public enum ECampaignDocumentType {
  DOCTYPE1("DOCTYPE1"),
  DOCTYPE2("DOCTYPE2"),
  DOCTYPE3("DOCTYPE3");

  private final String text;

  ECampaignDocumentType(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
