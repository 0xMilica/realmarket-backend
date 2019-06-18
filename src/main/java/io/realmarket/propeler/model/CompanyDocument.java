package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "company_document")
public class CompanyDocument {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPANY_DOCUMENT_SEQ")
  @SequenceGenerator(
      name = "COMPANY_DOCUMENT_SEQ",
      sequenceName = "COMPANY_DOCUMENT_SEQ",
      allocationSize = 1)
  private Long id;

  private String title;

  @JoinColumn(
      name = "accessLevelId",
      foreignKey = @ForeignKey(name = "company_document_fk_on_access_level"))
  @ManyToOne
  private DocumentAccessLevel accessLevel;

  @JoinColumn(name = "typeId", foreignKey = @ForeignKey(name = "company_document_fk_on_type"))
  @ManyToOne
  private CompanyDocumentType type;

  private String url;
  private Instant uploadDate;

  @JoinColumn(name = "companyId", foreignKey = @ForeignKey(name = "company_document_fk_on_company"))
  @ManyToOne
  private Company company;
}
