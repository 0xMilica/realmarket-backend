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
@Table(name = "campaign_team_member")
public class CampaignTeamMember {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAMPAIGN_TEAM_MEMBER_SEQ")
  @SequenceGenerator(
      name = "CAMPAIGN_TEAM_MEMBER_SEQ",
      sequenceName = "CAMPAIGN_TEAM_MEMBER_SEQ",
      allocationSize = 1)
  private Long id;

  private String name;
  private String title;
  private String description;
  private String photoUrl;
  private String linkedinUrl;
  private String twitterUrl;
  private String facebookUrl;
  private String customProfileUrl;
  private UnsignedInteger orderNumber;

  @JoinColumn(
      name = "campaignId",
      foreignKey = @ForeignKey(name = "campaign_team_member_fk_on_campaign"))
  @ManyToOne
  private Campaign campaign;
}
