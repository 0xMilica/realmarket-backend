package io.realmarket.propeler.service.blockchain.dto.company;

import io.realmarket.propeler.model.Shareholder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyShareholderDto {
  private Long shareholderId;
  private Boolean isAnonymous;
  private String name;
  private String location;
  private Double investedAmount;
  private String description;
  private String currency;
  private boolean isCompany;
  private String companyIdentificationNumber;

  public CompanyShareholderDto(Shareholder shareholder) {
    this.shareholderId = shareholder.getId();
    this.isAnonymous = shareholder.getIsAnonymous();
    this.name = shareholder.getName();
    this.location = shareholder.getLocation();
    this.investedAmount = shareholder.getInvestedAmount().doubleValue();
    this.description = shareholder.getDescription();
    this.currency = shareholder.getCurrency();
    this.isCompany = shareholder.isCompany();
    this.companyIdentificationNumber = shareholder.getCompanyIdentificationNumber();
  }
}
