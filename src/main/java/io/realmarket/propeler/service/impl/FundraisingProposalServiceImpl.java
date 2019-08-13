package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.FundraisingProposalDto;
import io.realmarket.propeler.api.dto.RejectionReasonDto;
import io.realmarket.propeler.api.dto.enums.EmailType;
import io.realmarket.propeler.model.FundraisingProposal;
import io.realmarket.propeler.model.RegistrationToken;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.FundraisingProposalRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.EmailService;
import io.realmarket.propeler.service.FundraisingProposalService;
import io.realmarket.propeler.service.RegistrationTokenService;
import io.realmarket.propeler.service.RequestStateService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.MailContentHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.FORBIDDEN_OPERATION_EXCEPTION;
import static io.realmarket.propeler.service.exception.util.ExceptionMessages.INVALID_REQUEST;

@Service
@Slf4j
public class FundraisingProposalServiceImpl implements FundraisingProposalService {

  private final FundraisingProposalRepository fundraisingProposalRepository;
  private final RegistrationTokenService registrationTokenService;
  private final RequestStateService requestStateService;
  private final EmailService emailService;

  @Autowired
  public FundraisingProposalServiceImpl(
      FundraisingProposalRepository fundraisingProposalRepository,
      RegistrationTokenService registrationTokenService,
      RequestStateService requestStateService,
      EmailService emailService) {
    this.fundraisingProposalRepository = fundraisingProposalRepository;
    this.registrationTokenService = registrationTokenService;
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
    if (filter == null) {
      return fundraisingProposalRepository.findAll(pageable);
    }
    return fundraisingProposalRepository.findByRequestState(
        pageable,
        requestStateService.getRequestState(RequestStateName.valueOf(filter.toUpperCase())));
  }

  @Override
  public FundraisingProposal approveFundraisingProposal(Long fundraisingProposalId) {
    throwIfNotAdmin();
    FundraisingProposal fundraisingProposal =
        fundraisingProposalRepository.getOne(fundraisingProposalId);
    throwIfNotPending(fundraisingProposal);

    fundraisingProposal.setRequestState(
        requestStateService.getRequestState(RequestStateName.APPROVED));

    fundraisingProposal = fundraisingProposalRepository.save(fundraisingProposal);

    RegistrationToken registrationToken = registrationTokenService.createToken(fundraisingProposal);
    sendApprovalEmail(fundraisingProposal, registrationToken);

    return fundraisingProposal;
  }

  @Override
  public FundraisingProposal declineFundraisingProposal(
      Long fundraisingProposalId, RejectionReasonDto rejectionReasonDto) {
    throwIfNotAdmin();
    FundraisingProposal fundraisingProposal =
        fundraisingProposalRepository.getOne(fundraisingProposalId);
    throwIfNotPending(fundraisingProposal);

    fundraisingProposal.setRequestState(
        requestStateService.getRequestState(RequestStateName.DECLINED));
    fundraisingProposal.setRejectionReason(rejectionReasonDto.getContent());

    fundraisingProposal = fundraisingProposalRepository.save(fundraisingProposal);
    sendRejectionEmail(fundraisingProposal);

    return fundraisingProposal;
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

  private void throwIfNotPending(FundraisingProposal fundraisingProposal) {
    if (!fundraisingProposal.getRequestState().getName().equals(RequestStateName.PENDING)) {
      throw new BadRequestException(INVALID_REQUEST);
    }
  }

  private void sendApprovalEmail(
      FundraisingProposal fundraisingProposal, RegistrationToken registrationToken) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put(EmailServiceImpl.FIRST_NAME, fundraisingProposal.getFirstName());
    parameters.put(EmailServiceImpl.LAST_NAME, fundraisingProposal.getLastName());
    parameters.put(EmailServiceImpl.REGISTRATION_TOKEN, registrationToken.getValue());
    emailService.sendMailToUser(
        new MailContentHolder(
            Collections.singletonList(fundraisingProposal.getEmail()),
            EmailType.FUNDRAISING_PROPOSAL_APPROVAL,
            parameters));
  }

  private void sendRejectionEmail(FundraisingProposal fundraisingProposal) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put(EmailServiceImpl.FIRST_NAME, fundraisingProposal.getFirstName());
    parameters.put(EmailServiceImpl.LAST_NAME, fundraisingProposal.getLastName());
    parameters.put(EmailServiceImpl.REJECTION_REASON, fundraisingProposal.getRejectionReason());
    emailService.sendMailToUser(
        new MailContentHolder(
            Collections.singletonList(fundraisingProposal.getEmail()),
            EmailType.FUNDRAISING_PROPOSAL_REJECTION,
            parameters));
  }
}
