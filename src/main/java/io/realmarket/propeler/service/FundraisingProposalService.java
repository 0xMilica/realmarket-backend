package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.AuditDeclineDto;
import io.realmarket.propeler.api.dto.FundraisingProposalDto;
import io.realmarket.propeler.api.dto.FundraisingProposalResponseDto;
import io.realmarket.propeler.model.FundraisingProposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FundraisingProposalService {

  FundraisingProposal findByIdOrThrowException(Long proposalId);

  FundraisingProposal applyForFundraising(FundraisingProposalDto fundraisingProposalDto);

  Page<FundraisingProposal> getFundraisingProposalsByState(Pageable pageable, String filter);

  FundraisingProposalResponseDto getFundraisingProposal(Long fundraisingProposalId);

  void approveFundraisingProposal(Long fundraisingProposalId);

  void declineFundraisingProposal(Long fundraisingProposalId, AuditDeclineDto auditDeclineDto);
}
