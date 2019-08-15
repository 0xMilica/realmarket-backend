package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "shareholder")
public class Shareholder {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SHAREHOLDER_SEQ")
  @SequenceGenerator(name = "SHAREHOLDER_SEQ", sequenceName = "SHAREHOLDER_SEQ", allocationSize = 1)
  private Long id;

  private Boolean isAnonymous;
  private String name;
  private String location;
  private BigDecimal investedAmount;
  private String description;
  private String photoUrl;
  private String linkedinUrl;
  private String twitterUrl;
  private String facebookUrl;
  private String customProfileUrl;
  private Integer orderNumber;
  private boolean isCompany;
  private String companyIdentificationNumber;

  @JoinColumn(name = "companyId", foreignKey = @ForeignKey(name = "shareholder_fk_on_company"))
  @ManyToOne
  private Company company;
}
