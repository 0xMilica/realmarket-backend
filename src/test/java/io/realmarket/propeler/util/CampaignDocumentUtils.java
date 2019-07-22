package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.CampaignDocumentDto;
import io.realmarket.propeler.model.CampaignDocument;
import io.realmarket.propeler.model.DocumentAccessLevel;
import io.realmarket.propeler.model.DocumentType;
import io.realmarket.propeler.model.enums.DocumentAccessLevelName;
import io.realmarket.propeler.model.enums.DocumentTypeName;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class CampaignDocumentUtils {

  public static final Long TEST_ID = 1L;
  public static final String TEST_TITLE = "TEST_TITLE";
  public static final DocumentAccessLevelName TEST_ACCESS_LEVEL_ENUM =
      DocumentAccessLevelName.PUBLIC;
  public static final DocumentTypeName TEST_TYPE_ENUM = DocumentTypeName.LEGAL;
  public static final DocumentAccessLevel TEST_ACCESS_LEVEL =
      DocumentAccessLevel.builder().name(TEST_ACCESS_LEVEL_ENUM).build();
  public static final DocumentType TEST_TYPE = DocumentType.builder().name(TEST_TYPE_ENUM).build();
  public static final String TEST_URL = "TEST_URL";
  public static final Instant TEST_UPLOAD_DATE = Instant.now();

  public static final String TEST_TITLE_2 = "TEST_TITLE_2";
  public static final DocumentAccessLevelName TEST_ACCESS_LEVEL_ENUM_2 =
      DocumentAccessLevelName.INVESTORS;
  public static final DocumentTypeName TEST_TYPE_ENUM_2 = DocumentTypeName.DUE_DILIGENCE;
  public static final DocumentAccessLevel TEST_ACCESS_LEVEL_2 =
      DocumentAccessLevel.builder().name(TEST_ACCESS_LEVEL_ENUM_2).build();
  public static final DocumentType TEST_TYPE_2 =
      DocumentType.builder().name(TEST_TYPE_ENUM_2).build();
  public static final String TEST_URL_2 = "TEST_URL2";

  public static final CampaignDocument TEST_CAMPAIGN_DOCUMENT =
      CampaignDocument.campaignDocumentBuilder()
          .title(TEST_TITLE)
          .accessLevel(TEST_ACCESS_LEVEL)
          .type(TEST_TYPE)
          .url(TEST_URL)
          .uploadDate(TEST_UPLOAD_DATE)
          .campaign(CampaignUtils.TEST_CAMPAIGN)
          .build();

  public static CampaignDocument getCampaignDocumentMocked() {
    return CampaignDocument.campaignDocumentBuilder()
        .title(TEST_TITLE)
        .accessLevel(TEST_ACCESS_LEVEL)
        .type(TEST_TYPE)
        .url(TEST_URL)
        .uploadDate(TEST_UPLOAD_DATE)
        .campaign(CampaignUtils.TEST_CAMPAIGN)
        .build();
  }

  public static CampaignDocument getCampaignDocumentMocked2() {
    return CampaignDocument.campaignDocumentBuilder()
        .title(TEST_TITLE_2)
        .accessLevel(TEST_ACCESS_LEVEL_2)
        .type(TEST_TYPE_2)
        .url(TEST_URL_2)
        .uploadDate(TEST_UPLOAD_DATE)
        .campaign(CampaignUtils.TEST_CAMPAIGN)
        .build();
  }

  public static CampaignDocument getCampaignDocumentMocked(
      DocumentAccessLevelName documentAccessLevel) {
    return CampaignDocument.campaignDocumentBuilder()
        .title(TEST_TITLE)
        .accessLevel(DocumentAccessLevel.builder().name(documentAccessLevel).build())
        .type(TEST_TYPE)
        .url(TEST_URL)
        .uploadDate(TEST_UPLOAD_DATE)
        .campaign(CampaignUtils.TEST_CAMPAIGN)
        .build();
  }

  public static CampaignDocumentDto getCampaignDocumentDtoMocked() {
    return CampaignDocumentDto.builder()
        .title(TEST_TITLE)
        .accessLevel(TEST_ACCESS_LEVEL_ENUM)
        .type(TEST_TYPE_ENUM)
        .url(TEST_URL)
        .build();
  }

  public static CampaignDocumentDto getCampaignDocumentDtoMocked2() {
    return CampaignDocumentDto.builder()
        .title(TEST_TITLE_2)
        .accessLevel(TEST_ACCESS_LEVEL_ENUM_2)
        .type(TEST_TYPE_ENUM_2)
        .url(TEST_URL_2)
        .build();
  }

  public static List<CampaignDocument> TEST_CAMPAIGN_DOCUMENT_LIST =
      Arrays.asList(
          getCampaignDocumentMocked(),
          getCampaignDocumentMocked(),
          getCampaignDocumentMocked(DocumentAccessLevelName.PLATFORM_ADMINS),
          getCampaignDocumentMocked(DocumentAccessLevelName.INVESTORS));
}
