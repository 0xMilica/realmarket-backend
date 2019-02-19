package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel("Two Factor Authorization secret generation request")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TwoFASecretRequestDto {

  @NotBlank(message = "Setup token not valid.")
  String setupToken;
}
