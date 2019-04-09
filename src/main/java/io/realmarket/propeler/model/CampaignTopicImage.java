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
    name = "campaign_topic_image",
    uniqueConstraints = {
      @UniqueConstraint(
          columnNames = {"url"},
          name = "campaign_topic_image_uk")
    })
public class CampaignTopicImage {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAMPAIGN_TOPIC_IMAGE_SEQ")
  @SequenceGenerator(
      name = "CAMPAIGN_TOPIC_IMAGE_SEQ",
      sequenceName = "CAMPAIGN_TOPIC_IMAGE_SEQ",
      allocationSize = 1)
  private Long id;

  @JoinColumn(
      name = "campaignTopicId",
      foreignKey = @ForeignKey(name = "campaign_topic_image_fk_on_campaign_topic"))
  @ManyToOne
  private CampaignTopic campaignTopic;

  private String url;
}
