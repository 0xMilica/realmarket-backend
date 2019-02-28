package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.ECampaignTopicType;
import io.realmarket.propeler.model.enums.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

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
          columnNames = {"type", "campaignId"},
          name = "campaign_topic_uk_on_type_and_campaignId")
    })
@TypeDef(name = "ecampaigntopictype", typeClass = PostgreSQLEnumType.class)
public class CampaignTopic {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAMPAIGN_TOPIC_SEQ")
  @SequenceGenerator(
      name = "CAMPAIGN_TOPIC_SEQ",
      sequenceName = "CAMPAIGN_TOPIC_SEQ",
      allocationSize = 1)
  private Long id;

  private String content;

  @Column(columnDefinition = "ecampaigntopictype")
  @Type(type = "ecampaigntopictype")
  @Enumerated(EnumType.STRING)
  private ECampaignTopicType type;

  @JoinColumn(name = "campaignId", foreignKey = @ForeignKey(name = "campaign_topic_fk_on_campaign"))
  @ManyToOne
  private Campaign campaign;
}
