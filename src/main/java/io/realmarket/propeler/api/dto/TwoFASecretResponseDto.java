package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("Encapsulates needed for reset password finalization.")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TwoFASecretResponseDto {
  private String secret;
  private String token;
}
