package io.realmarket.propeler.model.enums;

public enum DocumentTypeName {
  APR_PAPER("APR_PAPER"),
  BUSINESS_PLAN("BUSINESS_PLAN"),
  PITCH_DECK("PITCH_DECK"),
  BANK("BANK"),
  DUE_DILIGENCE("DUE_DILIGENCE"),
  LEGAL("LEGAL"),
  PERSONAL_ID_FRONT("PERSONAL_ID_FRONT"),
  PERSONAL_ID_BACK("PERSONAL_ID_BACK"),
  USER_KYC("USER_KYC"),
  CORPORATE_INVESTOR_KYC("CORPORATE_INVESTOR_KYC");

  private final String text;

  DocumentTypeName(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
