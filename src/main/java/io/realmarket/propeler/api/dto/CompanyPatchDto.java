package io.realmarket.propeler.api.dto;

import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.CompanyCategory;
import io.realmarket.propeler.model.CompanyEditRequest;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(
    value = "CompanyPatchDto",
    description = "Basic information of company that can be modified by owner")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyPatchDto {

  @ApiModelProperty(value = "Company's tax identifier")
  private String taxIdentifier;

  @ApiModelProperty(value = "Company's bank account")
  private String bankAccount;

  @ApiModelProperty(value = "Company's county")
  private String county;

  @ApiModelProperty(value = "Company's city")
  private String city;

  @ApiModelProperty(value = "Company's address")
  private String address;

  @ApiModelProperty(value = "Company's website")
  private String website;

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

  public Company buildCompany() {
    return Company.builder()
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

  public CompanyEditRequest buildCompanyEditRequest(Company company) {
    return CompanyEditRequest.builder()
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
        .company(company)
        .build();
  }

  public boolean shouldAdminBeCalled() {
    if (this.taxIdentifier != null) return true;
    if (this.bankAccount != null) return true;
    if (this.county != null) return true;
    if (this.city != null) return true;
    return this.address != null;
  }
}
