package io.realmarket.propeler.model;

import com.google.common.primitives.UnsignedInteger;
import io.realmarket.propeler.api.dto.CampaignDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(
    name = "campaign",
    indexes = {
      @Index(columnList = "name", unique = true, name = "campaign_uk_on_name"),
      @Index(columnList = "urlFriendlyName", unique = true, name = "campaign_uk_on_urlFriendlyName")
    })
public class Campaign {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAMPAIGN_SEQ")
  @SequenceGenerator(name = "CAMPAIGN_SEQ", sequenceName = "CAMPAIGN_SEQ", allocationSize = 1)
  private Long id;

  private String name;
  private String urlFriendlyName;
  private Long fundingGoals;
  private UnsignedInteger timeToRaiseFunds;
  private BigDecimal minEquityOffered;
  private BigDecimal maxEquityOffered;
  private String marketImageUrl;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "companyId", foreignKey = @ForeignKey(name = "campaign_fk_on_company"))
  private Company company;

  public Campaign() {}

  public Campaign(CampaignDto campaignDto) {
    this.name = campaignDto.getName();
    this.fundingGoals = campaignDto.getFundingGoals();
    this.timeToRaiseFunds = campaignDto.getTimeToRaiseFunds();
    this.minEquityOffered = campaignDto.getMinEquityOffered();
    this.maxEquityOffered = campaignDto.getMaxEquityOffered();
    this.urlFriendlyName = campaignDto.getUrlFriendlyName();
  }
}
