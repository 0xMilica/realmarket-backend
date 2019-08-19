package io.realmarket.propeler.service.blockchain.dto.company;

import io.realmarket.propeler.model.Shareholder;
import lombok.Data;

@Data
public class ShareholderDto {
  private Long shareholderId;
  private Boolean isAnonymous;
  private String name;
  private String location;
  private Double investedAmount;
  private String description;
  private boolean isCompany;
  private String companyIdentificationNumber;

  public ShareholderDto(Shareholder shareholder) {
    this.shareholderId = shareholder.getId();
    this.isAnonymous = shareholder.getIsAnonymous();
    this.name = shareholder.getName();
    this.location = shareholder.getLocation();
    this.investedAmount = shareholder.getInvestedAmount().doubleValue();
    this.description = shareholder.getDescription();
    this.isCompany = shareholder.isCompany();
    this.companyIdentificationNumber = shareholder.getCompanyIdentificationNumber();
  }
}
