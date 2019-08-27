package io.realmarket.propeler.service.blockchain.dto.company;

import io.realmarket.propeler.model.CompanyEditRequest;
import io.realmarket.propeler.service.blockchain.dto.AbstractBlockchainDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEditRequestDto extends AbstractBlockchainDto {
  private Long companyId;
  private EditRequestDetails editRequestDetails;

  public CompanyEditRequestDto(CompanyEditRequest companyEditRequest) {
    this.companyId = companyEditRequest.getCompany().getId();
    this.userId = companyEditRequest.getCompany().getAuth().getId();
    this.editRequestDetails =
        EditRequestDetails.builder()
            .name(companyEditRequest.getName())
            .taxIdentifier(companyEditRequest.getTaxIdentifier())
            .bankAccount(companyEditRequest.getBankAccount())
            .county(companyEditRequest.getCounty())
            .city(companyEditRequest.getCity())
            .address(companyEditRequest.getAddress())
            .build();
  }
}
