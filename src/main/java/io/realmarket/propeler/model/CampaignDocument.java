package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "campaign_document")
public class CampaignDocument {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAMPAIGN_DOCUMENT_SEQ")
  @SequenceGenerator(
      name = "CAMPAIGN_DOCUMENT_SEQ",
      sequenceName = "CAMPAIGN_DOCUMENT_SEQ",
      allocationSize = 1)
  private Long id;

  private String title;

  @JoinColumn(
      name = "accessLevelId",
      foreignKey = @ForeignKey(name = "campaign_document_fk_on_access_level"))
  @ManyToOne
  private DocumentAccessLevel accessLevel;

  @JoinColumn(name = "typeId", foreignKey = @ForeignKey(name = "campaign_document_fk_on_type"))
  @ManyToOne
  private CampaignDocumentType type;

  private String url;
  private Instant uploadDate;

  @JoinColumn(
      name = "campaignId",
      foreignKey = @ForeignKey(name = "campaign_document_fk_on_campaign"))
  @ManyToOne
  private Campaign campaign;
}
