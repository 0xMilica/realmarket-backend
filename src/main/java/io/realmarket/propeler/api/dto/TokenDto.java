package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel("Token")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

  @NotBlank(message = "Please provide valid token")
  String token;
}
