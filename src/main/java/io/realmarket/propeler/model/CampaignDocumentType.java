package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.ECampaignDocumentType;
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
    name = "campaign_document_type",
    indexes = {
      @Index(columnList = "name", unique = true, name = "campaign_document_type_uk_on_name"),
    })
public class CampaignDocumentType {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAMPAIGN_DOCUMENT_SEQ")
  @SequenceGenerator(
      name = "CAMPAIGN_DOCUMENT_SEQ",
      sequenceName = "CAMPAIGN_DOCUMENT_SEQ",
      allocationSize = 1)
  private Long id;

  @Enumerated(EnumType.STRING)
  private ECampaignDocumentType name;
}
