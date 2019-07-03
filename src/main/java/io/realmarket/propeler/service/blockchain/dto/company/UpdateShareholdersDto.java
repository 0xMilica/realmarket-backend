package io.realmarket.propeler.service.blockchain.dto.company;

import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.Shareholder;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UpdateShareholdersDto extends AbstractBlockchainDto {
  private Long companyId;
  private List<ShareholderDto> shareholders;

  public UpdateShareholdersDto(Company company, List<Shareholder> shareholders) {
    this.userId = company.getAuth().getId();
    this.companyId = company.getId();
    this.shareholders = shareholders.stream().map(ShareholderDto::new).collect(Collectors.toList());
  }
}
