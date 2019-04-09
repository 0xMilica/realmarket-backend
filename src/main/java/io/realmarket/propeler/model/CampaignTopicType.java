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
@Table(name = "campaign_topic_type")
public class CampaignTopicType {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAMPAIGN_TOPIC_TYPE_SEQ")
  @SequenceGenerator(
      name = "CAMPAIGN_TOPIC_TYPE_SEQ",
      sequenceName = "CAMPAIGN_TOPIC_TYPE_SEQ",
      allocationSize = 1)
  private Long id;

  private String name;
}
