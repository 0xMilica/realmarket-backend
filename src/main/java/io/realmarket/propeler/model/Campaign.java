package io.realmarket.propeler.model;

import com.google.common.primitives.UnsignedInteger;
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
    name = "campaign",
    indexes = {
      @Index(columnList = "name", unique = true, name = "campaign_uk_on_name"),
      @Index(columnList = "friendlyUrl", unique = true, name = "campaign_uk_on_friendlyUrl")
    })
public class Campaign {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAMPAIGN_SEQ")
  @SequenceGenerator(name = "CAMPAIGN_SEQ", sequenceName = "CAMPAIGN_SEQ", allocationSize = 1)
  private Long id;

  private String name;
  private String friendlyUrl;
  private Long fundingGoals;
  private UnsignedInteger timeToRaiseFunds;
  private Double minEquityOffered;
  private Double maxEquityOffered;
  private String marketImageUrl;
}
