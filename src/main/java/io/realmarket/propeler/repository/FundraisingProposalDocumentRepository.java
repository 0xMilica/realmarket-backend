package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.FundraisingProposal;
import io.realmarket.propeler.model.FundraisingProposalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FundraisingProposalDocumentRepository
    extends JpaRepository<FundraisingProposalDocument, Long> {

  List<FundraisingProposalDocument> findAllByFundraisingProposal(
      FundraisingProposal fundraisingProposal);
}
