package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.ECampaignDocumentAccessLevel;
import io.realmarket.propeler.model.enums.ECampaignDocumentType;
import io.realmarket.propeler.model.enums.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "campaign_document")
@TypeDef(name = "ecampaigndocumentaccesslevel", typeClass = PostgreSQLEnumType.class)
@TypeDef(name = "ecampaigndocumenttype", typeClass = PostgreSQLEnumType.class)
public class CampaignDocument {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAMPAIGN_DOCUMENT_SEQ")
  @SequenceGenerator(
      name = "CAMPAIGN_DOCUMENT_SEQ",
      sequenceName = "CAMPAIGN_DOCUMENT_SEQ",
      allocationSize = 1)
  private Long id;

  private String title;

  @Column(columnDefinition = "ecampaigndocumentaccesslevel")
  @Type(type = "ecampaigndocumentaccesslevel")
  @Enumerated(EnumType.STRING)
  private ECampaignDocumentAccessLevel accessLevel;

  @Column(columnDefinition = "ecampaigndocumenttype")
  @Type(type = "ecampaigndocumenttype")
  @Enumerated(EnumType.STRING)
  private ECampaignDocumentType type;

  private String url;
  private Instant uploadDate;

  @JoinColumn(
      name = "campaignId",
      foreignKey = @ForeignKey(name = "campaign_document_fk_on_campaign"))
  @ManyToOne
  private Campaign campaign;
}
