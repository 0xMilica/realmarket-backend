package io.realmarket.propeler.service.blockchain.dto.company;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyDetails {
  private Long companyId;
  private String name;
  private String taxIdentifier;
  private String bankAccount;
  private String category;
  private String county;
  private String city;
  private String address;
}
