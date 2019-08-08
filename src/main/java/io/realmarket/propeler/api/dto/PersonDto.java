package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.Person;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Size;

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

  @ApiModelProperty(value = "For corporate person, name of company this person represents")
  private String companyName;

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

  @ApiModelProperty(value = "Linkedin url")
  private String linkedinUrl;

  @ApiModelProperty(value = "Twitter url")
  private String twitterUrl;

  @ApiModelProperty(value = "Facebook url")
  private String facebookUrl;

  @ApiModelProperty(value = "Custom url")
  private String customProfileUrl;

  @ApiModelProperty(value = "Short Biography")
  @Size(max = 250, message = "Short biography cannot be longer than 250 characters.")
  private String shortBiography;

  public PersonDto(Person person) {
    this.id = person.getId();
    this.lastName = person.getLastName();
    this.firstName = person.getFirstName();
    this.companyName = person.getCompanyName();
    this.address = person.getAddress();
    this.city = person.getCity();
    if (!StringUtils.isEmpty(person.getCountryForTaxation())) {
      this.countryForTaxation = person.getCountryForTaxation().getCode();
    }
    this.countryOfResidence = person.getCountryOfResidence().getCode();
    this.email = person.getEmail();
    this.phoneNumber = person.getPhoneNumber();
    this.profilePictureUrl = person.getProfilePictureUrl();
    this.linkedinUrl = person.getLinkedinUrl();
    this.facebookUrl = person.getFacebookUrl();
    this.twitterUrl = person.getTwitterUrl();
    this.customProfileUrl = person.getCustomProfileUrl();
    this.shortBiography = person.getShortBiography();
  }
}
