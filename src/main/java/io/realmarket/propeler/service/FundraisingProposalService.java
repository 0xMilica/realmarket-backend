package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.FundraisingProposalDto;
import io.realmarket.propeler.model.FundraisingProposal;

public interface FundraisingProposalService {
  FundraisingProposal applyForFundraising(FundraisingProposalDto fundraisingProposalDto);
}
