package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.CompanyDto;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.CompanyCategory;

import static io.realmarket.propeler.util.AuthUtils.TEST_AUTH;

public class CompanyUtils {

  public static final Long TEST_ID = 1L;
  public static final String TEST_NAME = "TEST_NAME";
  public static final String TEST_TAX_IDENTIFIER = "TEST_TAX_IDENTIFIER";
  public static final String TEST_BANK_ACCOUNT = "TEST_BANK_ACCOUNT";
  public static final String TEST_COUNTY = "TEST_COUNTY";
  public static final String TEST_CITY = "TEST_CITY";
  public static final String TEST_ADDRESS = "TEST_ADDRESS";
  public static final String TEST_WEBSITE = "TEST_WEBSITE";
  public static final String TEST_LOGO_URL = "TEST_LOGO_URL";
  public static final String TEST_FEATURED_IMAGE_URL = "TEST_FEATURED_IMAGE_URL";

  public static final CompanyCategory TEST_CATEGORY_COMPANY =
      CompanyCategory.builder().id(TEST_ID).name(TEST_NAME).build();

  public static final Company TEST_COMPANY =
      Company.builder()
          .id(TEST_ID)
          .name(TEST_NAME)
          .taxIdentifier(TEST_TAX_IDENTIFIER)
          .bankAccount(TEST_BANK_ACCOUNT)
          .county(TEST_COUNTY)
          .city(TEST_CITY)
          .address(TEST_ADDRESS)
          .website(TEST_WEBSITE)
          .logoUrl(TEST_LOGO_URL)
          .featuredImageUrl(TEST_FEATURED_IMAGE_URL)
          .companyCategory(TEST_CATEGORY_COMPANY)
          .auth(TEST_AUTH)
          .build();

  public static Company getCompanyMocked() {
    return Company.builder()
        .id(TEST_ID)
        .name(TEST_NAME)
        .taxIdentifier(TEST_TAX_IDENTIFIER)
        .bankAccount(TEST_BANK_ACCOUNT)
        .county(TEST_COUNTY)
        .city(TEST_CITY)
        .address(TEST_ADDRESS)
        .website(TEST_WEBSITE)
        .logoUrl(TEST_LOGO_URL)
        .featuredImageUrl(TEST_FEATURED_IMAGE_URL)
        .companyCategory(TEST_CATEGORY_COMPANY)
        .auth(TEST_AUTH)
        .build();
  }

  public static CompanyDto getCompanyDtoMocked() {
    return CompanyDto.builder()
        .id(TEST_ID)
        .name(TEST_NAME)
        .taxIdentifier(TEST_TAX_IDENTIFIER)
        .bankAccount(TEST_BANK_ACCOUNT)
        .county(TEST_COUNTY)
        .city(TEST_CITY)
        .address(TEST_ADDRESS)
        .website(TEST_WEBSITE)
        .logoUrl(TEST_LOGO_URL)
        .featuredImageUrl(TEST_FEATURED_IMAGE_URL)
        .companyCategory(TEST_CATEGORY_COMPANY)
        .ownerId(TEST_AUTH.getId())
        .build();
  }
}
