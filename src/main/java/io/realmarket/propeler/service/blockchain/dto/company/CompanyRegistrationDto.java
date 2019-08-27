package io.realmarket.propeler.service.blockchain.dto.company;

import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRegistrationDto extends AbstractBlockchainDto {
  private CompanyDetails company;

  public CompanyRegistrationDto(Company company) {
    this.userId = company.getAuth().getId();
    this.company =
        CompanyDetails.builder()
            .companyId(company.getId())
            .name(company.getName())
            .taxIdentifier(company.getTaxIdentifier())
            .bankAccount(company.getBankAccount())
            .category(company.getCompanyCategory().getName())
            .county(company.getCounty())
            .city(company.getCity())
            .address(company.getAddress())
            .build();
  }
}
