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
@DiscriminatorValue("PersonDocument")
@Entity(name = "PersonDocument")
public class PersonDocument extends Document {

  @JoinColumn(name = "personId", foreignKey = @ForeignKey(name = "document_fk_on_person"))
  @ManyToOne
  private Person person;

  @Builder(builderMethodName = "personDocumentBuilder")
  public PersonDocument(
      String title,
      DocumentAccessLevel accessLevel,
      DocumentType type,
      String url,
      Instant uploadDate,
      Person person) {
    super(title, accessLevel, type, url, uploadDate);
    this.person = person;
  }
}
