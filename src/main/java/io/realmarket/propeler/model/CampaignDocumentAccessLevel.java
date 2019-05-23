package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.ECampaignDocumentAccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "campaign_document_access_level",
    indexes = {
      @Index(
          columnList = "name",
          unique = true,
          name = "campaign_document_access_level_uk_on_name"),
    })
public class CampaignDocumentAccessLevel {
  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "CAMPAIGN_DOCUMENT_ACCESS_LEVEL_SEQ")
  @SequenceGenerator(
      name = "CAMPAIGN_DOCUMENT_ACCESS_LEVEL_SEQ",
      sequenceName = "CAMPAIGN_DOCUMENT_ACCESS_LEVEL_SEQ",
      allocationSize = 1)
  private Long id;

  @Enumerated(EnumType.STRING)
  private ECampaignDocumentAccessLevel name;
}
