package io.realmarket.propeler.model;

import io.realmarket.propeler.api.dto.RegistrationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Builder
@Entity(name = "Person")
@Table(indexes = @Index(columnList = "email", name = "person_uk_on_email"))
public class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERSON_SEQ")
  @SequenceGenerator(name = "PERSON_SEQ", sequenceName = "PERSON_SEQ", allocationSize = 1)
  private Long id;

  private String email;
  private String firstName;
  private String lastName;
  private String countryOfResidence;
  private String countryForTaxation;
  private String city;
  private String address;
  private String phoneNumber;
  private String profilePictureUrl;

  @OneToOne(
      mappedBy = "person",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      optional = false)
  private Auth auth;

  public Person() {}

  public Person(RegistrationDto registrationDto) {
    this.email = registrationDto.getEmail();
    this.firstName = registrationDto.getFirstName();
    this.lastName = registrationDto.getLastName();
    this.countryOfResidence = registrationDto.getCountryOfResidence();
    this.countryForTaxation = registrationDto.getCountryForTaxation();
    this.city = registrationDto.getCity();
    this.address = registrationDto.getAddress();
    this.phoneNumber = registrationDto.getPhoneNumber();
  }
}
