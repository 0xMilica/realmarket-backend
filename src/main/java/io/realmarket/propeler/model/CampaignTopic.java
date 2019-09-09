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
    name = "campaign_topic",
    uniqueConstraints = {
      @UniqueConstraint(
          columnNames = {"campaignTopicTypeId", "campaignId"},
          name = "campaign_topic_uk_on_type_and_campaign_id")
    })
public class CampaignTopic {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAMPAIGN_TOPIC_SEQ")
  @SequenceGenerator(
      name = "CAMPAIGN_TOPIC_SEQ",
      sequenceName = "CAMPAIGN_TOPIC_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(length = 100000)
  private String content;

  @JoinColumn(
      name = "campaignTopicTypeId",
      foreignKey = @ForeignKey(name = "campaign_topic_fk_on_campaign_topic_type"))
  @ManyToOne
  private CampaignTopicType campaignTopicType;

  @JoinColumn(name = "campaignId", foreignKey = @ForeignKey(name = "campaign_topic_fk_on_campaign"))
  @ManyToOne
  private Campaign campaign;
}
