package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Person")
@Table(indexes = @Index(columnList = "email"))
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
      fetch = FetchType.LAZY,
      optional = false)
  private Auth auth;
}
