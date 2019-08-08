package io.realmarket.propeler.model.enums;

import java.util.Arrays;
import java.util.List;

public enum UserRoleName {
  ROLE_ADMIN("ROLE_ADMIN"),
  ROLE_INDIVIDUAL_INVESTOR("ROLE_INDIVIDUAL_INVESTOR"),
  ROLE_CORPORATE_INVESTOR("ROLE_CORPORATE_INVESTOR"),
  ROLE_ENTREPRENEUR("ROLE_ENTREPRENEUR"),
  ROLE_AUDITOR("ROLE_AUDITOR");

  private final String text;

  UserRoleName(final String text) {
    this.text = text;
  }

  public static List<UserRoleName> getInvestorRoleNames() {
    return Arrays.asList(ROLE_INDIVIDUAL_INVESTOR, ROLE_CORPORATE_INVESTOR);
  }

  @Override
  public String toString() {
    return text;
  }
}
