package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "campaign_investor")
public class CampaignInvestor {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAMPAIGN_INVESTOR_SEQ")
  @SequenceGenerator(
      name = "CAMPAIGN_INVESTOR_SEQ",
      sequenceName = "CAMPAIGN_INVESTOR_SEQ",
      allocationSize = 1)
  private Long id;

  private Boolean isAnonymous;
  private String name;
  private String location;
  private BigDecimal investedAmount;
  private String description;
  private String photoUrl;
  private String linkedinUrl;
  private String twitterUrl;
  private String facebookUrl;
  private String customProfileUrl;
  private Integer orderNumber;

  @JoinColumn(
      name = "campaignId",
      foreignKey = @ForeignKey(name = "campaign_investor_fk_on_campaign"))
  @ManyToOne
  private Campaign campaign;
}
