package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "User's username.")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsernameDto {
  @NotBlank(message = "You must provide username!")
  private String username;
}
