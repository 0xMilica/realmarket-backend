package io.realmarket.propeler.service.blockchain.dto.company;

import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.Shareholder;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyUpdateShareholdersDto extends AbstractBlockchainDto {
  private Long companyId;
  private List<CompanyShareholderDto> shareholders;

  public CompanyUpdateShareholdersDto(Company company, List<Shareholder> shareholders) {
    this.userId = company.getAuth().getId();
    this.companyId = company.getId();
    this.shareholders =
        shareholders.stream().map(CompanyShareholderDto::new).collect(Collectors.toList());
  }
}
