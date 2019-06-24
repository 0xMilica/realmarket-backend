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
@Table(name = "campaign_update")
public class CampaignUpdate {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAMPAIGN_UPDATE_SEQ")
  @SequenceGenerator(
      name = "CAMPAIGN_UPDATE_SEQ",
      sequenceName = "CAMPAIGN_UPDATE_SEQ",
      allocationSize = 1)
  private Long id;

  @JoinColumn(
      name = "campaignId",
      foreignKey = @ForeignKey(name = "campaign_update_fk_on_campaign"))
  @ManyToOne
  private Campaign campaign;

  private String title;
  private Instant postDate;

  @Column(length = 10000)
  private String content;
}
