package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.CampaignTeamMember;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Dto used for transfer of campaign team member data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignTeamMemberDto {
  private Long id;

  private String name;
  private String title;
  private String description;
  private String photoUrl;
  private String linkedinUrl;
  private String twitterUrl;
  private String facebookUrl;
  private String customProfileUrl;
  private Integer orderNumber;

  public CampaignTeamMemberDto(CampaignTeamMember campaignTeamMember) {
    this.id = campaignTeamMember.getId();
    this.name = campaignTeamMember.getName();
    this.title = campaignTeamMember.getTitle();
    this.description = campaignTeamMember.getDescription();
    this.photoUrl = campaignTeamMember.getPhotoUrl();
    this.linkedinUrl = campaignTeamMember.getLinkedinUrl();
    this.twitterUrl = campaignTeamMember.getTwitterUrl();
    this.facebookUrl = campaignTeamMember.getFacebookUrl();
    this.customProfileUrl = campaignTeamMember.getCustomProfileUrl();
    this.orderNumber = campaignTeamMember.getOrderNumber();
  }
}
