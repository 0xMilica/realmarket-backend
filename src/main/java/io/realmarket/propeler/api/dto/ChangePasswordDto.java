package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "Information needed for password change.")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordDto {
  @NotBlank(message = "Please provide old password")
  @ApiModelProperty(value = "Old password")
  String oldPassword;

  @NotBlank(message = "Please provide new password")
  @ApiModelProperty(value = "New password")
  String newPassword;
}
