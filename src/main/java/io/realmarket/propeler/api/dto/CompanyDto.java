package io.realmarket.propeler.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.CompanyCategory;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "CompanyDto", description = "Basic information about company")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDto {

  @ApiModelProperty(value = "Company's identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @ApiModelProperty(value = "Company's name")
  @NotBlank
  private String name;

  @ApiModelProperty(value = "Company's tax identifier")
  @NotBlank
  private String taxIdentifier;

  @ApiModelProperty(value = "Company's bank account")
  @NotBlank
  private String bankAccount;

  @ApiModelProperty(value = "Company's county")
  private String county;

  @ApiModelProperty(value = "Company's city")
  private String city;

  @ApiModelProperty(value = "Company's address")
  private String address;

  @ApiModelProperty(value = "Company's website")
  private String website;

  @ApiModelProperty(value = "Company's logo URL")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String logoUrl;

  @ApiModelProperty(value = "Company's logo URL")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String featuredImageUrl;

  @ApiModelProperty(value = "Company's linkedin URL")
  private String linkedinUrl;

  @ApiModelProperty(value = "Company's twitter URL")
  private String twitterUrl;

  @ApiModelProperty(value = "Company's facebook URL")
  private String facebookUrl;

  @ApiModelProperty(value = "Company's custom url")
  private String customUrl;

  @ApiModelProperty(value = "Company's category")
  private CompanyCategory companyCategory;

  @ApiModelProperty(value = "Company's owner identifier")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long ownerId;

  public CompanyDto(Company company) {
    this.id = company.getId();
    this.name = company.getName();
    this.taxIdentifier = company.getTaxIdentifier();
    this.bankAccount = company.getBankAccount();
    this.county = company.getCounty();
    this.city = company.getCity();
    this.address = company.getAddress();
    this.website = company.getWebsite();
    this.logoUrl = company.getLogoUrl();
    this.featuredImageUrl = company.getFeaturedImageUrl();
    this.linkedinUrl = company.getLinkedinUrl();
    this.twitterUrl = company.getTwitterUrl();
    this.facebookUrl = company.getFacebookUrl();
    this.customUrl = company.getCustomUrl();
    this.companyCategory = company.getCompanyCategory();
    this.ownerId = company.getAuth().getId();
  }

  public Company buildCompany() {
    return Company.builder()
        .name(this.getName())
        .taxIdentifier(this.getTaxIdentifier())
        .bankAccount(this.getBankAccount())
        .county(this.getCounty())
        .city(this.getCity())
        .address(this.getAddress())
        .website(this.getWebsite())
        .linkedinUrl(this.getLinkedinUrl())
        .twitterUrl(this.getTwitterUrl())
        .facebookUrl(this.getFacebookUrl())
        .customUrl(this.getCustomUrl())
        .companyCategory(this.getCompanyCategory())
        .auth(AuthenticationUtil.getAuthentication().getAuth())
        .build();
  }
}
