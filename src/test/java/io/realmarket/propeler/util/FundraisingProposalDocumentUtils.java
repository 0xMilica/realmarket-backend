package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.FundraisingProposalDocumentDto;
import io.realmarket.propeler.model.CompanyDocumentType;
import io.realmarket.propeler.model.FundraisingProposalDocument;
import io.realmarket.propeler.model.enums.CompanyDocumentTypeName;

import java.time.Instant;

public class FundraisingProposalDocumentUtils {

  public static final Long TEST_ID = 1L;
  public static final String TEST_TITLE = "TEST_TITLE";
  public static final CompanyDocumentTypeName TEST_TYPE_ENUM =
      CompanyDocumentTypeName.DOCTYPE_PITCH_DECK;
  public static final CompanyDocumentType TEST_TYPE =
      CompanyDocumentType.builder().name(TEST_TYPE_ENUM).build();
  public static final String TEST_URL = "TEST_URL";
  public static final Instant TEST_UPLOAD_DATE = Instant.now();

  public static final FundraisingProposalDocument TEST_FUNDRAISING_PROPOSAL_DOCUMENT =
      FundraisingProposalDocument.builder()
          .id(TEST_ID)
          .title(TEST_TITLE)
          .type(TEST_TYPE)
          .url(TEST_URL)
          .uploadDate(TEST_UPLOAD_DATE)
          .fundraisingProposal(FundraisingProposalUtils.TEST_PENDING_FUNDRAISING_PROPOSAL)
          .build();

  public static FundraisingProposalDocumentDto TEST_FUNDRAISING_PROPOSAL_DOCUMENT_DTO =
      FundraisingProposalDocumentDto.builder()
          .title(TEST_TITLE)
          .type(TEST_TYPE_ENUM)
          .url(TEST_URL)
          .build();
}
