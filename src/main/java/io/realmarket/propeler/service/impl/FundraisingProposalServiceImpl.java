package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.FundraisingProposalDto;
import io.realmarket.propeler.model.FundraisingProposal;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.FundraisingProposalRepository;
import io.realmarket.propeler.service.FundraisingProposalService;
import io.realmarket.propeler.service.RequestStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundraisingProposalServiceImpl implements FundraisingProposalService {

  private final FundraisingProposalRepository fundraisingProposalRepository;
  private final RequestStateService requestStateService;

  @Autowired
  public FundraisingProposalServiceImpl(
      FundraisingProposalRepository fundraisingProposalRepository,
      RequestStateService requestStateService) {
    this.fundraisingProposalRepository = fundraisingProposalRepository;
    this.requestStateService = requestStateService;
  }

  @Override
  public FundraisingProposal applyForFundraising(FundraisingProposalDto fundraisingProposalDto) {
    FundraisingProposal fundraisingProposal = new FundraisingProposal(fundraisingProposalDto);
    fundraisingProposal.setRequestState(
        requestStateService.getRequestState(RequestStateName.PENDING));
    return fundraisingProposalRepository.save(fundraisingProposal);
  }
}
