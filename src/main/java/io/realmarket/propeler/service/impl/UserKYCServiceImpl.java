package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.api.dto.enums.EmailType;
import io.realmarket.propeler.model.*;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.UserKYCRepository;
import io.realmarket.propeler.repository.UserRoleRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.blockchain.BlockchainCommunicationService;
import io.realmarket.propeler.service.blockchain.BlockchainMethod;
import io.realmarket.propeler.service.blockchain.dto.user.kyc.ChangeStateDto;
import io.realmarket.propeler.service.blockchain.dto.user.kyc.RequestForReviewDto;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.MailContentHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.*;

@Service
public class UserKYCServiceImpl implements UserKYCService {

  private final PersonService personService;
  private final RequestStateService requestStateService;
  private final CompanyService companyService;
  private final EmailService emailService;
  private final UserKYCDocumentService userKYCDocumentService;
  private final UserKYCRepository userKYCRepository;
  private final UserRoleRepository userRoleRepository;
  private final BlockchainCommunicationService blockchainCommunicationService;

  @Value(value = "${app.time.zone}")
  private String timeZone;

  public UserKYCServiceImpl(
      PersonService personService,
      RequestStateService requestStateService,
      CompanyService companyService,
      EmailService emailService,
      UserKYCDocumentService userKYCDocumentService,
      UserKYCRepository userKYCRepository,
      UserRoleRepository userRoleRepository,
      BlockchainCommunicationService blockchainCommunicationService) {
    this.personService = personService;
    this.requestStateService = requestStateService;
    this.companyService = companyService;
    this.emailService = emailService;
    this.userKYCDocumentService = userKYCDocumentService;
    this.userKYCRepository = userKYCRepository;
    this.userRoleRepository = userRoleRepository;
    this.blockchainCommunicationService = blockchainCommunicationService;
  }

  @Override
  public UserKYC createUserKYCRequest(UserKYCRequestDto userKYCRequestDto) {
    UserKYC userKYC =
        UserKYC.builder()
            .user(AuthenticationUtil.getAuthentication().getAuth())
            .requestState(requestStateService.getRequestState(RequestStateName.PENDING))
            .uploadDate(Instant.now())
            .politicallyExposed(userKYCRequestDto.isPoliticallyExposed())
            .build();

    userKYC = userKYCRepository.save(userKYC);

    userKYC = userKYCDocumentService.submitDocuments(userKYC, userKYCRequestDto.getDocumentsUrl());

    blockchainCommunicationService.invoke(
        BlockchainMethod.USER_KYC_REQUEST_FOR_REVIEW,
        new RequestForReviewDto(userKYC, AuthenticationUtil.getAuthentication().getAuth().getId()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());

    return userKYC;
  }

  @Override
  public UserKYC assignUserKYC(UserKYCAssignmentDto userKYCAssignmentDto) {
    // TODO: Instead of taking Auth from session, it should be taken from userKYCAssignmentDto when
    // auditor listing is implemented
    Auth auditorAuth = AuthenticationUtil.getAuthentication().getAuth();
    // TODO: Change this condition when assigning become possible for other roles too
    if (!auditorAuth.getUserRole().getName().equals(UserRoleName.ROLE_ADMIN)) {
      throw new BadRequestException(USER_CAN_NOT_BE_AUDITOR);
    }
    UserKYC userKYC = userKYCRepository.getOne(userKYCAssignmentDto.getUserKYCId());
    if (userKYC.getAuditor() != null) {
      throw new BadRequestException(INVALID_REQUEST);
    }

    userKYC.setAuditor(auditorAuth);
    userKYC = userKYCRepository.save(userKYC);
    sendKYCUnderReviewMail(userKYC);

    blockchainCommunicationService.invoke(
        BlockchainMethod.USER_KYC_STATE_CHANGE,
        new ChangeStateDto(userKYC, AuthenticationUtil.getAuthentication().getAuth().getId()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());

    return userKYC;
  }

  private void sendKYCUnderReviewMail(UserKYC userKYC) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put(EmailServiceImpl.FIRST_NAME, userKYC.getUser().getPerson().getFirstName());
    parameters.put(EmailServiceImpl.LAST_NAME, userKYC.getUser().getPerson().getLastName());

    emailService.sendMailToUser(
        new MailContentHolder(
            Collections.singletonList(userKYC.getUser().getPerson().getEmail()),
            EmailType.KYC_UNDER_REVIEW,
            parameters));
  }

  @Override
  public UserKYCResponseWithFilesDto getUserKYC(Long userKYCId) {
    UserKYC userKYC = findByIdOrThrowException(userKYCId);
    throwIfNoAccess(userKYC);

    return convertUserKYCToUserKYCResponseWithFilesDto(userKYC);
  }

  @Override
  public Page<UserKYCResponseDto> getUserKYCs(Pageable pageable, String requestState, String role) {
    boolean isRoleAll = role.equals("all");
    boolean isStateAll = requestState.equals("all");
    Page<UserKYC> results;
    if (isStateAll && isRoleAll) results = userKYCRepository.findAll(pageable);
    else if (isStateAll) {
      UserRole userRole = getUserRole(role);
      results = userKYCRepository.findAllByUserRole(pageable, userRole);
    } else if (isRoleAll) {
      RequestState state;
      if (requestState.toLowerCase().startsWith("pending")) state = getUserKYCState("PENDING");
      else state = getUserKYCState(requestState);
      if (requestState.toLowerCase().endsWith("notassigned"))
        results = userKYCRepository.findAllByRequestStateNotAssigned(pageable, state);
      else results = userKYCRepository.findAllByRequestStateAssigned(pageable, state);
    } else {
      UserRole userRole = getUserRole(role);
      RequestState state;
      if (requestState.toLowerCase().startsWith("pending")) state = getUserKYCState("PENDING");
      else state = getUserKYCState(requestState);
      if (requestState.toLowerCase().endsWith("notassigned"))
        results =
            userKYCRepository.findAllByRequestStateAndByUserRoleNotAssigned(
                pageable, state, userRole);
      else
        results =
            userKYCRepository.findAllByRequestStateAndByUserRoleAssigned(pageable, state, userRole);
    }
    return results.map(this::convertUserKYCToUserKYCResponseDto);
  }

  @Override
  public UserKYC approveUserKYC(Long userKYCId) {
    UserKYC userKYC = findByIdOrThrowException(userKYCId);
    throwIfNotPending(userKYC);
    throwIfNotAuditor(userKYC);
    userKYC.setRequestState(requestStateService.getRequestState(RequestStateName.APPROVED));
    userKYC = userKYCRepository.save(userKYC);
    sendKYCApprovalEmail(userKYC);

    blockchainCommunicationService.invoke(
        BlockchainMethod.USER_KYC_STATE_CHANGE,
        new ChangeStateDto(userKYC, AuthenticationUtil.getAuthentication().getAuth().getId()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());

    return userKYC;
  }

  private void sendKYCApprovalEmail(UserKYC userKYC) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put(EmailServiceImpl.FIRST_NAME, userKYC.getUser().getPerson().getFirstName());
    parameters.put(EmailServiceImpl.LAST_NAME, userKYC.getUser().getPerson().getLastName());

    LocalDateTime uploadDate =
        LocalDateTime.ofInstant(userKYC.getUploadDate(), ZoneId.of(timeZone));
    parameters.put(EmailServiceImpl.DATE, uploadDate);

    emailService.sendMailToUser(
        new MailContentHolder(
            Collections.singletonList(userKYC.getUser().getPerson().getEmail()),
            EmailType.KYC_APPROVAL,
            parameters));
  }

  @Override
  public UserKYC rejectUserKYC(Long userKYCId, String rejectionReason) {
    UserKYC userKYC = findByIdOrThrowException(userKYCId);
    throwIfNotPending(userKYC);
    throwIfNotAuditor(userKYC);
    userKYC.setRequestState(requestStateService.getRequestState(RequestStateName.DECLINED));
    userKYC.setRejectionReason(rejectionReason);
    userKYC = userKYCRepository.save(userKYC);
    sendKYCRejectionEmail(userKYC);

    blockchainCommunicationService.invoke(
        BlockchainMethod.USER_KYC_STATE_CHANGE,
        new ChangeStateDto(userKYC, AuthenticationUtil.getAuthentication().getAuth().getId()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());

    return userKYC;
  }

  private void sendKYCRejectionEmail(UserKYC userKYC) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put(EmailServiceImpl.FIRST_NAME, userKYC.getUser().getPerson().getFirstName());
    parameters.put(EmailServiceImpl.LAST_NAME, userKYC.getUser().getPerson().getLastName());
    parameters.put(EmailServiceImpl.REJECTION_REASON, userKYC.getRejectionReason());

    LocalDateTime uploadDate =
        LocalDateTime.ofInstant(userKYC.getUploadDate(), ZoneId.of(timeZone));
    parameters.put(EmailServiceImpl.DATE, uploadDate);

    emailService.sendMailToUser(
        new MailContentHolder(
            Collections.singletonList(userKYC.getUser().getPerson().getEmail()),
            EmailType.KYC_REJECTION,
            parameters));
  }

  private void throwIfNotPending(UserKYC userKYC) {
    if (!userKYC.getRequestState().getName().equals(RequestStateName.PENDING)) {
      throw new BadRequestException(INVALID_REQUEST);
    }
  }

  private void throwIfNoAccess(UserKYC userKYC) {
    UserRoleName userRoleName =
        AuthenticationUtil.getAuthentication().getAuth().getUserRole().getName();
    if ((userRoleName.equals(UserRoleName.ROLE_ENTREPRENEUR)
            || userRoleName.equals(UserRoleName.ROLE_INVESTOR))
        && !isOwner(userKYC)) {
      throw new ForbiddenOperationException(USER_IS_NOT_OWNER_OF_KYC);
    }
  }

  private boolean isOwner(UserKYC userKYC) {
    return userKYC
        .getUser()
        .getId()
        .equals(AuthenticationUtil.getAuthentication().getAuth().getId());
  }

  private UserRole getUserRole(String role) {
    return userRoleRepository
        .findByName(getUserRoleNameOrThrow(role))
        .orElseThrow(EntityNotFoundException::new);
  }

  private UserRoleName getUserRoleNameOrThrow(String role) {
    if (role.equals("investor")) {
      return UserRoleName.ROLE_INVESTOR;
    } else if (role.equals("entrepreneur")) return UserRoleName.ROLE_ENTREPRENEUR;
    throw new BadRequestException(INVALID_REQUEST);
  }

  private RequestState getUserKYCState(String state) {
    return requestStateService.getRequestState(state);
  }

  private void throwIfNotAuditor(UserKYC userKYC) {
    if (userKYC.getAuditor() == null) {
      throw new ForbiddenOperationException(USER_KYC_AUDITOR_NOT_ASSIGNED);
    }
    if (!AuthenticationUtil.getAuthentication()
        .getAuth()
        .getId()
        .equals(userKYC.getAuditor().getId())) {
      throw new ForbiddenOperationException(NOT_USER_KYC_AUDITOR);
    }
  }

  private UserKYC findByIdOrThrowException(Long id) {
    return userKYCRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException(USER_KYC_DOES_NOT_EXIST));
  }

  private UserKYCResponseWithFilesDto convertUserKYCToUserKYCResponseWithFilesDto(UserKYC userKYC) {
    Auth user = userKYC.getUser();
    Person person = personService.getPersonFromAuth(user);
    Company company = null;
    if (user.getUserRole().getName().equals(UserRoleName.ROLE_ENTREPRENEUR)) {
      company = companyService.findByAuthIdOrThrowException(user.getId());
    }
    List<UserKYCDocument> userKYCDocuments = userKYCDocumentService.findByUserKYC(userKYC);
    return new UserKYCResponseWithFilesDto(
        userKYC,
        person,
        user,
        company,
        userKYCDocuments.stream().map(DocumentResponseDto::new).collect(Collectors.toList()));
  }

  private UserKYCResponseDto convertUserKYCToUserKYCResponseDto(UserKYC userKYC) {
    Auth user = userKYC.getUser();
    Person person = personService.getPersonFromAuth(user);
    Company company = null;
    if (user.getUserRole().getName().equals(UserRoleName.ROLE_ENTREPRENEUR))
      company = companyService.findByAuthIdOrThrowException(user.getId());
    return new UserKYCResponseDto(userKYC, person, user, company);
  }
}
