package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.CampaignStateName;
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
    name = "campaign_state",
    indexes = {@Index(columnList = "name", unique = true, name = "campaign_state_uk_on_name")})
public class CampaignState {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAMPAIGN_STATE_SEQ")
  @SequenceGenerator(
      name = "CAMPAIGN_STATE_SEQ",
      sequenceName = "CAMPAIGN_STATE_SEQ",
      allocationSize = 1)
  private Long id;

  @Enumerated(EnumType.STRING)
  private CampaignStateName name;
}
