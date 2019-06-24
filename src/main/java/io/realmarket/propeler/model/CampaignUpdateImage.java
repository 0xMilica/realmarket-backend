package io.realmarket.propeler.model;

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
    name = "campaign_update_image",
    uniqueConstraints = {
      @UniqueConstraint(
          columnNames = {"url"},
          name = "campaign_update_image_uk")
    })
public class CampaignUpdateImage {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAMPAIGN_UPDATE_IMAGE_SEQ")
  @SequenceGenerator(
      name = "CAMPAIGN_UPDATE_IMAGE_SEQ",
      sequenceName = "CAMPAIGN_UPDATE_IMAGE_SEQ",
      allocationSize = 1)
  private Long id;

  @JoinColumn(
      name = "campaignUpdateId",
      foreignKey = @ForeignKey(name = "campaign_update_image_fk_on_campaign_update"))
  @ManyToOne
  private CampaignUpdate campaignUpdate;

  private String url;
}
