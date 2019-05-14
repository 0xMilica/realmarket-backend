package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.api.dto.CampaignDocumentDto;
import io.realmarket.propeler.model.CampaignDocument;
import io.realmarket.propeler.model.enums.ECampaignDocumentAccessLevel;
import io.realmarket.propeler.model.enums.ECampaignDocumentType;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class CampaignDocumentUtils {

  public static final Long TEST_ID = 1L;
  public static final String TEST_TITLE = "TEST_TITLE";
  public static final ECampaignDocumentAccessLevel TEST_ACCESS_LEVEL =
      ECampaignDocumentAccessLevel.PUBLIC;
  public static final ECampaignDocumentType TEST_TYPE = ECampaignDocumentType.DOCTYPE_APR_PAPER;
  public static final String TEST_URL = "TEST_URL";
  public static final Instant TEST_UPLOAD_DATE = Instant.now();

  public static CampaignDocument getCampaignDocumentMocked() {
    return CampaignDocument.builder()
        .id(TEST_ID)
        .title(TEST_TITLE)
        .accessLevel(TEST_ACCESS_LEVEL)
        .type(TEST_TYPE)
        .url(TEST_URL)
        .uploadDate(TEST_UPLOAD_DATE)
        .campaign(CampaignUtils.TEST_CAMPAIGN)
        .build();
  }

  public static CampaignDocument getCampaignDocumentMocked(
      ECampaignDocumentAccessLevel eCampaignDocumentAccessLevel) {
    return CampaignDocument.builder()
        .id(TEST_ID)
        .title(TEST_TITLE)
        .accessLevel(eCampaignDocumentAccessLevel)
        .type(TEST_TYPE)
        .url(TEST_URL)
        .uploadDate(TEST_UPLOAD_DATE)
        .campaign(CampaignUtils.TEST_CAMPAIGN)
        .build();
  }

  public static CampaignDocumentDto getCampaignDocumentDtoMocked() {
    return CampaignDocumentDto.builder()
        .title(TEST_TITLE)
        .accessLevel(TEST_ACCESS_LEVEL)
        .type(TEST_TYPE)
        .url(TEST_URL)
        .uploadDate(TEST_UPLOAD_DATE)
        .build();
  }

  public static List<CampaignDocument> TEST_CAMPAIGN_DOCUMENT_LIST =
      Arrays.asList(getCampaignDocumentMocked(), getCampaignDocumentMocked(),getCampaignDocumentMocked(ECampaignDocumentAccessLevel.PLATFORM_ADMINS),
              getCampaignDocumentMocked(ECampaignDocumentAccessLevel.INVESTORS));
}
