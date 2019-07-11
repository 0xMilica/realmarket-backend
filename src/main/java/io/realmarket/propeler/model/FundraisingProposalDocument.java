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
@Table(name = "fundraising_proposal_document")
public class FundraisingProposalDocument {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "FUNDRAISING_PROPOSAL_DOCUMENT_SEQ")
  @SequenceGenerator(
      name = "FUNDRAISING_PROPOSAL_DOCUMENT_SEQ",
      sequenceName = "FUNDRAISING_PROPOSAL_DOCUMENT_SEQ",
      allocationSize = 1)
  private Long id;

  private String title;

  @JoinColumn(
      name = "typeId",
      foreignKey = @ForeignKey(name = "fundraising_proposal_document_fk_on_type"))
  @ManyToOne
  private CompanyDocumentType type;

  private String url;
  private Instant uploadDate;

  @JoinColumn(
      name = "fundraisingProposalId",
      foreignKey = @ForeignKey(name = "fundraising_proposal_document_fk_on_fundraising_proposal"))
  @ManyToOne
  private FundraisingProposal fundraisingProposal;
}
