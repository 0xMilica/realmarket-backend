package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("Id of newly created team member")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewTeamMemberIdDto {
  private Long id;
}
