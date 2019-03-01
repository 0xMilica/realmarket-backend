package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel("Password")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDto {

  @NotNull(message = "Please provide password")
  @Size(min = 8, message = "Password must be at least 8 characters long.")
  String password;
}
