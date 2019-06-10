package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "campaign_investment")
public class CampaignInvestment {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAMPAIGN_INVESTMENT_SEQ")
  @SequenceGenerator(
      name = "CAMPAIGN_INVESTMENT_SEQ",
      sequenceName = "CAMPAIGN_INVESTMENT_SEQ",
      allocationSize = 1)
  private Long id;

  private double investedAmount;

  @JoinColumn(name = "authId", foreignKey = @ForeignKey(name = "campaign_investment_fk_on_auth"))
  @ManyToOne
  private Auth auth;

  @JoinColumn(
      name = "campaignId",
      foreignKey = @ForeignKey(name = "campaign_investment_fk_on_campaign"))
  @ManyToOne
  private Campaign campaign;
}
