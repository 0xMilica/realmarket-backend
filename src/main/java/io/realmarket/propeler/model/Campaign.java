package io.realmarket.propeler.model;

import io.realmarket.propeler.api.dto.CampaignDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(
    name = "campaign",
    indexes = {@Index(columnList = "urlFriendlyName")})
public class Campaign {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAMPAIGN_SEQ")
  @SequenceGenerator(name = "CAMPAIGN_SEQ", sequenceName = "CAMPAIGN_SEQ", allocationSize = 1)
  private Long id;

  private String name;
  private String urlFriendlyName;
  private Long fundingGoals;
  private Integer timeToRaiseFunds;
  private BigDecimal minEquityOffered;
  private BigDecimal maxEquityOffered;
  private String marketImageUrl;
  private BigDecimal minInvestment;
  private String tagLine;
  @Column(name = "creation_date")
  private Timestamp creationDate = new Timestamp(System.currentTimeMillis());

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "companyId", foreignKey = @ForeignKey(name = "campaign_fk_on_company"))
  private Company company;

  @JoinColumn(
      name = "campaignStateId",
      foreignKey = @ForeignKey(name = "campaign_fk_on_campaign_state"))
  @ManyToOne
  private CampaignState campaignState;

  public Campaign() {}

  public Campaign(CampaignDto campaignDto) {
    this.name = campaignDto.getName();
    this.fundingGoals = campaignDto.getFundingGoals();
    this.timeToRaiseFunds = campaignDto.getTimeToRaiseFunds();
    this.minEquityOffered = campaignDto.getMinEquityOffered();
    this.maxEquityOffered = campaignDto.getMaxEquityOffered();
    this.urlFriendlyName = campaignDto.getUrlFriendlyName();
    this.minInvestment = campaignDto.getMinInvestment();
    this.tagLine = campaignDto.getTagLine();
  }
}
