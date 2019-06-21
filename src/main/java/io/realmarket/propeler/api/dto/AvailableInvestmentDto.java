package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@ApiModel("Login credentials.")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableInvestmentDto {
  private BigDecimal amount;
  private BigDecimal equity;
}
