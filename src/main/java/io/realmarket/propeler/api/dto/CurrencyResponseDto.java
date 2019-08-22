package io.realmarket.propeler.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Currency;
import java.util.Locale;

@ApiModel(value = "CurrencyResponseDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyResponseDto {

  private String code;
  private String name;
  private String symbol;

  public CurrencyResponseDto(Currency currency, String localeLanguage, String localeCountry) {
    code = currency.getCurrencyCode();
    name = currency.getDisplayName();
    symbol = currency.getSymbol(new Locale(localeLanguage, localeCountry));
  }
}
