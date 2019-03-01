package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.Person;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "PersonDto", description = "Person profile information")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {

  @ApiModelProperty(value = "Person's identifier")
  private Long id;

  @ApiModelProperty(value = "Person's email")
  private String email;

  @ApiModelProperty(value = "Person's first name")
  private String firstName;

  @ApiModelProperty(value = "Person's last name")
  private String lastName;

  @ApiModelProperty(value = "Person's country of residence")
  private String countryOfResidence;

  @ApiModelProperty(value = "Person's country for taxation")
  private String countryForTaxation;

  @ApiModelProperty(value = "Person's city")
  private String city;

  @ApiModelProperty(value = "Person's address")
  private String address;

  @ApiModelProperty(value = "Person's phone number")
  private String phoneNumber;

  @ApiModelProperty(value = "Person's profile picture url")
  private String profilePictureUrl;

  public PersonDto(Person person) {
    this.id = person.getId();
    this.lastName = person.getLastName();
    this.firstName = person.getFirstName();
    this.address = person.getAddress();
    this.city = person.getCity();
    this.countryForTaxation = person.getCountryForTaxation();
    this.countryOfResidence = person.getCountryOfResidence();
    this.email = person.getEmail();
    this.phoneNumber = person.getPhoneNumber();
    this.profilePictureUrl = person.getProfilePictureUrl();
  }
}
