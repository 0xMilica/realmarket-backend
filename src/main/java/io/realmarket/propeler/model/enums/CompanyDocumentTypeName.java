package io.realmarket.propeler.model.enums;

public enum CompanyDocumentTypeName {
  DOCTYPE_APR_PAPER("DOCTYPE_APR_PAPER"),
  DOCTYPE_BUSINESS_PLAN("DOCTYPE_BUSINESS_PLAN"),
  DOCTYPE_PITCH_DECK("DOCTYPE_PITCH_DECK"),
  DOCTYPE_BANK("DOCTYPE_BANK");

  private final String text;

  CompanyDocumentTypeName(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
