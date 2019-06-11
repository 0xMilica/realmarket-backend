package io.realmarket.propeler.service.util.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageDto {

  private String[] receivers;

  @NotNull private String subject;

  @NotNull private String text;
}
