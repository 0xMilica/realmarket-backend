package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.PlatformSettings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatformSettingsDto {

  private BigDecimal minInvestment;

  public PlatformSettingsDto(PlatformSettings settings) {
    this.minInvestment = settings.getPlatformMinimalInvestment();
  }
}
