package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.DocumentTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "DocumentType")
@Table(
    name = "document_type",
    indexes = {
      @Index(columnList = "name", unique = true, name = "document_type_uk_on_name"),
    })
public class DocumentType {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENT_TYPE_SEQ")
  @SequenceGenerator(
      name = "DOCUMENT_TYPE_SEQ",
      sequenceName = "DOCUMENT_TYPE_SEQ",
      allocationSize = 1)
  private Long id;

  @Enumerated(EnumType.STRING)
  private DocumentTypeName name;
}
