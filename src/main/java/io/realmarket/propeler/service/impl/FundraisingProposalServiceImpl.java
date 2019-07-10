package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.AuditDeclineDto;
import io.realmarket.propeler.api.dto.FundraisingProposalDto;
import io.realmarket.propeler.api.dto.FundraisingProposalResponseDto;
import io.realmarket.propeler.model.FundraisingProposal;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.FundraisingProposalRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.FundraisingProposalService;
import io.realmarket.propeler.service.RequestStateService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.FORBIDDEN_OPERATION_EXCEPTION;

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

  @Override
  public FundraisingProposalResponseDto getFundraisingProposal(Long fundraisingProposalId) {
    return new FundraisingProposalResponseDto(
        fundraisingProposalRepository.getOne(fundraisingProposalId));
  }

  @Override
  public void declineFundraisingProposal(
      Long fundraisingProposalId, AuditDeclineDto auditDeclineDto) {
    throwIfNotAdmin();
    FundraisingProposal fundraisingProposal =
        fundraisingProposalRepository.getOne(fundraisingProposalId);

    fundraisingProposal.setRequestState(
        requestStateService.getRequestState(RequestStateName.DECLINED));
    fundraisingProposal.setContent(auditDeclineDto.getContent());

    fundraisingProposalRepository.save(fundraisingProposal);
  }

  @Override
  public void approveFundraisingProposal(Long fundraisingProposalId) {
    throwIfNotAdmin();
    FundraisingProposal fundraisingProposal =
        fundraisingProposalRepository.getOne(fundraisingProposalId);

    fundraisingProposal.setRequestState(
        requestStateService.getRequestState(RequestStateName.APPROVED));

    fundraisingProposalRepository.save(fundraisingProposal);
  }

  private void throwIfNotAdmin() {
    if (!isAdmin()) {
      throw new ForbiddenOperationException(FORBIDDEN_OPERATION_EXCEPTION);
    }
  }

  private boolean isAdmin() {
    return AuthenticationUtil.getAuthentication()
        .getAuth()
        .getUserRole()
        .getName()
        .equals(UserRoleName.ROLE_ADMIN);
  }
}
