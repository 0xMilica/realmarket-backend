package io.realmarket.propeler.service.blockchain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HashedPersonDetails {
  private String email;
  private String firstName;
  private String lastName;
  private String countryOfResidence;
  private String countryOfTaxation;
  private String city;
  private String address;
  private String phoneNumber;
}
