package io.realmarket.propeler.service.blockchain.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditRequestDetails {
  private String name;
  private String taxIdentifier;
  private String bankAccount;
  private String county;
  private String city;
  private String address;
}
