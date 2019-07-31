package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.FundraisingProposalDto;
import io.realmarket.propeler.api.dto.RejectionReasonDto;
import io.realmarket.propeler.model.FundraisingProposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FundraisingProposalService {

  FundraisingProposal findByIdOrThrowException(Long proposalId);

  FundraisingProposal applyForFundraising(FundraisingProposalDto fundraisingProposalDto);

  Page<FundraisingProposal> getFundraisingProposalsByState(Pageable pageable, String filter);

  FundraisingProposal approveFundraisingProposal(Long fundraisingProposalId);

  FundraisingProposal declineFundraisingProposal(
      Long fundraisingProposalId, RejectionReasonDto rejectionReasonDto);
}
