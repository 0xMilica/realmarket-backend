package io.realmarket.propeler.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "companyEdit")
public class CompanyEditRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPANY_EDIT_SEQ")
  @SequenceGenerator(
      name = "COMPANY_EDIT_SEQ",
      sequenceName = "COMPANY_EDIT_SEQ",
      allocationSize = 1)
  private Long id;

  private String name;
  private String taxIdentifier;
  private String bankAccount;
  private String county;
  private String city;
  private String address;
  private String website;
  private String linkedinUrl;
  private String twitterUrl;
  private String facebookUrl;
  private String customUrl;

  @Column(name = "request_timestamp")
  private Instant requestTimestamp = Instant.now();

  @JoinColumn(
      name = "companyCategoryId",
      foreignKey = @ForeignKey(name = "company_fk_on_company_category"))
  @ManyToOne
  private CompanyCategory companyCategory;

  @NotNull
  @JoinColumn(name = "companyId", foreignKey = @ForeignKey(name = "company_edit_fk_on_company"))
  @ManyToOne
  private Company company;

  @JoinColumn(
      name = "requestStateId",
      foreignKey = @ForeignKey(name = "company_edit_request_fk_on_request_state"))
  @ManyToOne
  private RequestState requestState;
}
