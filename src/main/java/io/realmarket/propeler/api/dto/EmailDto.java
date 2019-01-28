package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.api.dto.enums.EEmailType;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@ApiModel("Encapsulates email address information.")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
  @NotBlank private String email;
  @NotNull private EEmailType type;
  private Map<String, Object> content;
}
