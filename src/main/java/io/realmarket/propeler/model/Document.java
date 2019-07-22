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
@Entity(name = "Document")
@Table(name = "document")
@DiscriminatorColumn(name = "Entity_name")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Document {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENT_SEQ")
  @SequenceGenerator(name = "DOCUMENT_SEQ", sequenceName = "DOCUMENT_SEQ", allocationSize = 1)
  private Long id;

  private String title;

  @JoinColumn(
      name = "accessLevelId",
      foreignKey = @ForeignKey(name = "document_fk_on_access_level"))
  @ManyToOne
  private DocumentAccessLevel accessLevel;

  @JoinColumn(name = "typeId", foreignKey = @ForeignKey(name = "document_fk_on_type"))
  @ManyToOne
  private DocumentType type;

  private String url;
  private Instant uploadDate;

  public Document(
      String title,
      DocumentAccessLevel accessLevel,
      DocumentType type,
      String url,
      Instant uploadDate) {
    this.title = title;
    this.accessLevel = accessLevel;
    this.type = type;
    this.url = url;
    this.uploadDate = uploadDate;
  }
}
