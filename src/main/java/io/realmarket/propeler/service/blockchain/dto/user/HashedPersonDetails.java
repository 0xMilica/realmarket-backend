package io.realmarket.propeler.service.blockchain.dto.user;

import io.realmarket.propeler.model.Person;
import lombok.Data;

import static io.realmarket.propeler.service.util.HashingHelper.hash;

@Data
public class HashedPersonDetails {
  private String email;
  private String firstName;
  private String lastName;
  private String countryOfResidence;
  private String countryOfTaxation;
  private String city;
  private String address;
  private String phoneNumber;
  private String companyName;
  private String companyIdentificationNumber;

  public HashedPersonDetails(Person person) {
    this.email = hash(person.getEmail());
    this.firstName = hash(person.getFirstName());
    this.lastName = hash(person.getLastName());
    this.countryOfResidence = hash(person.getCountryOfResidence().getCode());
    this.city = hash(person.getCity());
    this.address = hash(person.getAddress());
    this.companyName = hash(person.getCompanyName());
    this.companyIdentificationNumber = hash(person.getCompanyIdentificationNumber());

    if (person.getCountryForTaxation() != null) {
      this.countryOfTaxation = person.getCountryForTaxation().getCode();
    }

    if (person.getPhoneNumber() != null) {
      this.phoneNumber = hash(person.getPhoneNumber());
    }
  }
}
