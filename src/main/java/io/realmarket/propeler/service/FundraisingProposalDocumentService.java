package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.FundraisingProposalDocumentDto;
import io.realmarket.propeler.api.dto.FundraisingProposalDocumentResponseDto;
import io.realmarket.propeler.model.FundraisingProposal;
import io.realmarket.propeler.model.FundraisingProposalDocument;

import java.util.List;

public interface FundraisingProposalDocumentService {

  FundraisingProposalDocument findByIdOrThrowException(Long documentId);

  List<FundraisingProposalDocument> findAllByFundraisingProposal(
      FundraisingProposal fundraisingProposal);

  FundraisingProposalDocument submitDocument(
      FundraisingProposalDocumentDto fundraisingProposalDocumentDto, Long proposalId);

  List<FundraisingProposalDocumentResponseDto> getFundraisingProposalDocuments(Long proposalId);
}
