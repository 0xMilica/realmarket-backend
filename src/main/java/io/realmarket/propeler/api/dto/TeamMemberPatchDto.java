package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Dto used for recieveng request data of campaign team member")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberPatchDto {

  private String name;
  private String title;
  private String description;
  private String linkedinUrl;
  private String twitterUrl;
  private String facebookUrl;
  private String customProfileUrl;
}
