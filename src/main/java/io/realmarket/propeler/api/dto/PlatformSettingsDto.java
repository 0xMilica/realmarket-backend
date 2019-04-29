package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.PlatformSettings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformSettingsDto {

  private BigDecimal platformMinInvestment;

  public PlatformSettingsDto(PlatformSettings settings) {
    this.platformMinInvestment = settings.getPlatformMinInvestment();
  }
}
