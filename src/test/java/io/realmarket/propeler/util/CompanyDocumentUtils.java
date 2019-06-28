package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.CompanyDocumentDto;
import io.realmarket.propeler.model.CompanyDocument;
import io.realmarket.propeler.model.CompanyDocumentType;
import io.realmarket.propeler.model.DocumentAccessLevel;
import io.realmarket.propeler.model.enums.CompanyDocumentTypeName;
import io.realmarket.propeler.model.enums.DocumentAccessLevelName;

import java.time.Instant;

public class CompanyDocumentUtils {

  public static final Long TEST_ID = 1L;
  public static final String TEST_TITLE = "TEST_TITLE";
  public static final DocumentAccessLevelName TEST_ACCESS_LEVEL_ENUM =
      DocumentAccessLevelName.PUBLIC;
  public static final CompanyDocumentTypeName TEST_TYPE_ENUM =
      CompanyDocumentTypeName.DOCTYPE_APR_PAPER;
  public static final DocumentAccessLevel TEST_ACCESS_LEVEL =
      DocumentAccessLevel.builder().name(TEST_ACCESS_LEVEL_ENUM).build();
  public static final CompanyDocumentType TEST_TYPE =
      CompanyDocumentType.builder().name(TEST_TYPE_ENUM).build();
  public static final String TEST_URL = "TEST_URL";
  public static final Instant TEST_UPLOAD_DATE = Instant.now();

  public static final Long TEST_ID_2 = 2L;
  public static final String TEST_TITLE_2 = "TEST_TITLE_2";
  public static final DocumentAccessLevelName TEST_ACCESS_LEVEL_ENUM_2 =
      DocumentAccessLevelName.INVESTORS;
  public static final CompanyDocumentTypeName TEST_TYPE_ENUM_2 =
      CompanyDocumentTypeName.DOCTYPE_BANK;
  public static final DocumentAccessLevel TEST_ACCESS_LEVEL_2 =
      DocumentAccessLevel.builder().name(TEST_ACCESS_LEVEL_ENUM_2).build();
  public static final CompanyDocumentType TEST_TYPE_2 =
      CompanyDocumentType.builder().name(TEST_TYPE_ENUM_2).build();
  public static final String TEST_URL_2 = "TEST_URL2";

  public static final CompanyDocument TEST_COMPANY_DOCUMENT =
      CompanyDocument.builder()
          .id(TEST_ID)
          .title(TEST_TITLE)
          .accessLevel(TEST_ACCESS_LEVEL)
          .type(TEST_TYPE)
          .url(TEST_URL)
          .uploadDate(TEST_UPLOAD_DATE)
          .company(CompanyUtils.TEST_COMPANY)
          .build();

  public static CompanyDocument getCompanyDocumentMocked() {
    return CompanyDocument.builder()
        .id(TEST_ID)
        .title(TEST_TITLE)
        .accessLevel(TEST_ACCESS_LEVEL)
        .type(TEST_TYPE)
        .url(TEST_URL)
        .uploadDate(TEST_UPLOAD_DATE)
        .company(CompanyUtils.TEST_COMPANY)
        .build();
  }

  public static CompanyDocumentDto getCompanyDocumentDtoMocked() {
    return CompanyDocumentDto.builder()
        .title(TEST_TITLE)
        .accessLevel(TEST_ACCESS_LEVEL_ENUM)
        .type(TEST_TYPE_ENUM)
        .url(TEST_URL)
        .build();
  }

  public static CompanyDocument getCompanyDocumentMocked2() {
    return CompanyDocument.builder()
        .id(TEST_ID_2)
        .title(TEST_TITLE_2)
        .accessLevel(TEST_ACCESS_LEVEL_2)
        .type(TEST_TYPE_2)
        .url(TEST_URL_2)
        .uploadDate(TEST_UPLOAD_DATE)
        .company(CompanyUtils.TEST_COMPANY)
        .build();
  }

  public static CompanyDocumentDto getCompanyDocumentDtoMocked2() {
    return CompanyDocumentDto.builder()
        .title(TEST_TITLE_2)
        .accessLevel(TEST_ACCESS_LEVEL_ENUM_2)
        .type(TEST_TYPE_ENUM_2)
        .url(TEST_URL_2)
        .build();
  }
}
