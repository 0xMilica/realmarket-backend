package io.realmarket.propeler.model;

import io.realmarket.propeler.api.dto.RegistrationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
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

  @JoinColumn(
      name = "countryOfResidence",
      foreignKey = @ForeignKey(name = "person_fk_on_country_of_residence"))
  @ManyToOne
  private Country countryOfResidence;

  @JoinColumn(
      name = "countryForTaxation",
      foreignKey = @ForeignKey(name = "person_fk_on_country_for_taxation"))
  @ManyToOne
  private Country countryForTaxation;

  private String city;
  private String address;
  private String phoneNumber;
  private String profilePictureUrl;

  @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Auth auth;

  public Person(
      RegistrationDto registrationDto, Country countryOfResidence, Country countryForTaxation) {
    this.email = registrationDto.getEmail();
    this.firstName = registrationDto.getFirstName();
    this.lastName = registrationDto.getLastName();
    this.countryOfResidence = countryOfResidence;
    this.countryForTaxation = countryForTaxation;
    this.city = registrationDto.getCity();
    this.address = registrationDto.getAddress();
    this.phoneNumber = registrationDto.getPhoneNumber();
  }
}
