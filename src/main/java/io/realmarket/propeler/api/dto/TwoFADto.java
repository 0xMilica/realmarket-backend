package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("2fa code for verification.")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwoFADto {
  private String code;
  private String wildcard;
}
