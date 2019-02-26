package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel("Token, 2fa code and remember me for a second step of login.")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginTwoFADto {
  @NotBlank(message = "Please provide token")
  private String token;

  private String code;

  private String wildcard;

  @Builder.Default
  private Boolean rememberMe = false;
}
