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
@DiscriminatorValue("CompanyDocument")
@Entity(name = "CompanyDocument")
public class CompanyDocument extends Document {

  @JoinColumn(name = "companyId", foreignKey = @ForeignKey(name = "document_fk_on_company"))
  @ManyToOne
  private Company company;

  @Builder(builderMethodName = "companyDocumentBuilder")
  public CompanyDocument(
      String title,
      DocumentAccessLevel accessLevel,
      DocumentType type,
      String url,
      Instant uploadDate,
      Company company) {
    super(title, accessLevel, type, url, uploadDate);
    this.company = company;
  }
}
