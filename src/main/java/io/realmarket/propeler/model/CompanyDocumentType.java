package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.ECompanyDocumentType;
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
@Table(
    name = "company_document_type",
    indexes = {
      @Index(columnList = "name", unique = true, name = "company_document_type_uk_on_name"),
    })
public class CompanyDocumentType {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPANY_DOCUMENT_TYPE_SEQ")
  @SequenceGenerator(
      name = "COMPANY_DOCUMENT_TYPE_SEQ",
      sequenceName = "COMPANY_DOCUMENT_TYPE_SEQ",
      allocationSize = 1)
  private Long id;

  @Enumerated(EnumType.STRING)
  private ECompanyDocumentType name;
}
