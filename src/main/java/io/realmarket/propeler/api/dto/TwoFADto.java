package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@ApiModel("2fa code for verification.")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwoFADto {

  @NotNull(message = "Provide your code or use wildcard")
  private String code;

  @NotNull(message = "Provide your code or use wildcard")
  private String wildcard;
}
