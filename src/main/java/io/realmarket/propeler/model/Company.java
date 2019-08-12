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
@Entity
@Table(name = "company")
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPANY_SEQ")
  @SequenceGenerator(name = "COMPANY_SEQ", sequenceName = "COMPANY_SEQ", allocationSize = 1)
  private Long id;

  private String name;
  private String taxIdentifier;
  private String bankAccount;
  private String companyIdentificationNumber;
  private String county;
  private String city;
  private String address;
  private String website;
  private String logoUrl;
  private String featuredImageUrl;
  private String linkedinUrl;
  private String twitterUrl;
  private String facebookUrl;
  private String customUrl;

  @JoinColumn(
      name = "companyCategoryId",
      foreignKey = @ForeignKey(name = "company_fk_on_company_category"))
  @ManyToOne
  private CompanyCategory companyCategory;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "authId", foreignKey = @ForeignKey(name = "company_fk_on_auth"))
  private Auth auth;
}
