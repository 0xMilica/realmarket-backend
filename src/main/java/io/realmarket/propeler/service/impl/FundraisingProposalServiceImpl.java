package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.AuditDeclineDto;
import io.realmarket.propeler.api.dto.FundraisingProposalDto;
import io.realmarket.propeler.api.dto.FundraisingProposalResponseDto;
import io.realmarket.propeler.api.dto.enums.EmailType;
import io.realmarket.propeler.model.FundraisingProposal;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.FundraisingProposalRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.EmailService;
import io.realmarket.propeler.service.FundraisingProposalService;
import io.realmarket.propeler.service.RequestStateService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.MailContentHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.FORBIDDEN_OPERATION_EXCEPTION;

@Service
@Slf4j
public class FundraisingProposalServiceImpl implements FundraisingProposalService {

  private final FundraisingProposalRepository fundraisingProposalRepository;
  private final RequestStateService requestStateService;
  private final EmailService emailService;

  @Autowired
  public FundraisingProposalServiceImpl(
      FundraisingProposalRepository fundraisingProposalRepository,
      RequestStateService requestStateService,
      EmailService emailService) {
    this.fundraisingProposalRepository = fundraisingProposalRepository;
    this.requestStateService = requestStateService;
    this.emailService = emailService;
  }

  @Override
  public FundraisingProposal findByIdOrThrowException(Long proposalId) {
    return fundraisingProposalRepository
        .findById(proposalId)
        .orElseThrow(EntityNotFoundException::new);
  }

  @Override
  public FundraisingProposal applyForFundraising(FundraisingProposalDto fundraisingProposalDto) {
    FundraisingProposal fundraisingProposal = new FundraisingProposal(fundraisingProposalDto);
    fundraisingProposal.setRequestState(
        requestStateService.getRequestState(RequestStateName.PENDING));
    return fundraisingProposalRepository.save(fundraisingProposal);
  }

  @Override
  public Page<FundraisingProposal> getFundraisingProposalsByState(
      Pageable pageable, String filter) {
    if (filter.equals("all")) return fundraisingProposalRepository.findAll(pageable);
    return fundraisingProposalRepository.findByRequestState(
        pageable, requestStateService.getRequestState(filter.toUpperCase()));
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
  public FundraisingProposalResponseDto approveFundraisingProposal(Long fundraisingProposalId) {
    throwIfNotAdmin();
    FundraisingProposal fundraisingProposal =
        fundraisingProposalRepository.getOne(fundraisingProposalId);

    fundraisingProposal.setRequestState(
        requestStateService.getRequestState(RequestStateName.APPROVED));

    fundraisingProposal = fundraisingProposalRepository.save(fundraisingProposal);
    sendApprovalEmail(fundraisingProposal.getEmail());

    return new FundraisingProposalResponseDto(fundraisingProposal);
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

  private void sendApprovalEmail(String email) {
    emailService.sendMailToUser(
        new MailContentHolder(
            Arrays.asList(email),
            EmailType.FUNDRAISING_PROPOSAL,
            Collections.singletonMap(EmailServiceImpl.FUNDRAISING_PROPOSAL, "")));
  }
}
