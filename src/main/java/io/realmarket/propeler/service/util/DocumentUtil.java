package io.realmarket.propeler.service.util;

import com.google.common.collect.ImmutableMap;
import io.realmarket.propeler.model.DocumentMetadata;
import io.realmarket.propeler.model.enums.DocumentAccessLevelName;
import io.realmarket.propeler.model.enums.DocumentTypeName;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DocumentUtil {
  public static final Map<String, List<DocumentMetadata>> documentEntityTypes =
      ImmutableMap.of(
          "companies",
          Arrays.asList(
              new DocumentMetadata(
                  DocumentTypeName.APR_PAPER,
                  true,
                  Collections.singletonList(DocumentAccessLevelName.PUBLIC),
                  1),
              new DocumentMetadata(
                  DocumentTypeName.BUSINESS_PLAN,
                  true,
                  Collections.singletonList(DocumentAccessLevelName.PUBLIC),
                  1),
              new DocumentMetadata(
                  DocumentTypeName.PITCH_DECK,
                  true,
                  Collections.singletonList(DocumentAccessLevelName.PUBLIC),
                  1),
              new DocumentMetadata(
                  DocumentTypeName.BANK,
                  true,
                  Collections.singletonList(DocumentAccessLevelName.PUBLIC),
                  1)),
          "campaigns",
          Arrays.asList(
              new DocumentMetadata(
                  DocumentTypeName.DUE_DILIGENCE,
                  true,
                  Collections.singletonList(DocumentAccessLevelName.PUBLIC),
                  1),
              new DocumentMetadata(
                  DocumentTypeName.LEGAL,
                  true,
                  Collections.singletonList(DocumentAccessLevelName.PUBLIC),
                  1)),
          "fundraisingProposals",
          Arrays.asList(
              new DocumentMetadata(
                  DocumentTypeName.BUSINESS_PLAN,
                  true,
                  Arrays.asList(DocumentAccessLevelName.PRIVATE, DocumentAccessLevelName.ON_DEMAND),
                  1),
              new DocumentMetadata(
                  DocumentTypeName.PITCH_DECK,
                  true,
                  Arrays.asList(DocumentAccessLevelName.PUBLIC, DocumentAccessLevelName.ON_DEMAND),
                  1)),
          "persons",
          Arrays.asList(
              new DocumentMetadata(
                  DocumentTypeName.PERSONAL_ID_FRONT,
                  true,
                  Collections.singletonList(DocumentAccessLevelName.PLATFORM_ADMINS),
                  1),
              new DocumentMetadata(
                  DocumentTypeName.PERSONAL_ID_BACK,
                  true,
                  Collections.singletonList(DocumentAccessLevelName.PLATFORM_ADMINS),
                  1)));

  private DocumentUtil() {}
}
