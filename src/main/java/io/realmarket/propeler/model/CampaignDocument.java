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
@DiscriminatorValue("CampaignDocument")
@Entity(name = "CampaignDocument")
public class CampaignDocument extends Document {

  @JoinColumn(name = "campaignId", foreignKey = @ForeignKey(name = "document_fk_on_campaign"))
  @ManyToOne
  private Campaign campaign;

  @Builder(builderMethodName = "campaignDocumentBuilder")
  public CampaignDocument(
      String title,
      DocumentAccessLevel accessLevel,
      DocumentType type,
      String url,
      Instant uploadDate,
      Campaign campaign) {
    super(title, accessLevel, type, url, uploadDate);
    this.campaign = campaign;
  }
}
