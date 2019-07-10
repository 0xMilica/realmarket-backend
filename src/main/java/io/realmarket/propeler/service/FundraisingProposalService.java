package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.AuditDeclineDto;
import io.realmarket.propeler.api.dto.FundraisingProposalDto;
import io.realmarket.propeler.api.dto.FundraisingProposalResponseDto;
import io.realmarket.propeler.model.FundraisingProposal;

public interface FundraisingProposalService {
  FundraisingProposal applyForFundraising(FundraisingProposalDto fundraisingProposalDto);

  FundraisingProposalResponseDto getFundraisingProposal(Long fundraisingProposalId);

  void approveFundraisingProposal(Long fundraisingProposalId);

  void declineFundraisingProposal(Long fundraisingProposalId, AuditDeclineDto auditDeclineDto);
}
