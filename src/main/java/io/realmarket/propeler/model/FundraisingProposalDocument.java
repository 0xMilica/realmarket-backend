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
@DiscriminatorValue("FundraisingProposalDocument")
@Entity(name = "FundraisingProposalDocument")
public class FundraisingProposalDocument extends Document {

  @JoinColumn(
      name = "fundraisingProposalId",
      foreignKey = @ForeignKey(name = "document_fk_on_fundraising_proposal"))
  @ManyToOne
  private FundraisingProposal fundraisingProposal;

  @Builder(builderMethodName = "fundraisingProposalDocumentBuilder")
  public FundraisingProposalDocument(
      String title,
      DocumentAccessLevel accessLevel,
      DocumentType type,
      String url,
      Instant uploadDate,
      FundraisingProposal fundraisingProposal) {
    super(title, accessLevel, type, url, uploadDate);
    this.fundraisingProposal = fundraisingProposal;
  }
}
