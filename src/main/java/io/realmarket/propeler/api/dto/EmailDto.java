package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.api.annotations.Email;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel("Encapsulates e-mail information.")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDto {

  @Email
  @NotBlank(message = "Please provide e-mail address")
  private String email;
}
