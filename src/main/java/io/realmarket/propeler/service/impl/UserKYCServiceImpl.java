package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.api.dto.enums.EmailType;
import io.realmarket.propeler.model.*;
import io.realmarket.propeler.model.enums.NotificationType;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.UserKYCRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.blockchain.BlockchainMethod;
import io.realmarket.propeler.service.blockchain.dto.user.kyc.KYCChangeStateDto;
import io.realmarket.propeler.service.blockchain.dto.user.kyc.KYCRequestForReviewDto;
import io.realmarket.propeler.service.blockchain.queue.BlockchainMessageProducer;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.MailContentHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  private final UserRoleService userRoleService;
  private final UserKYCRepository userKYCRepository;
  private final BlockchainMessageProducer blockchainMessageProducer;
  private final NotificationService notificationService;

  @Value(value = "${app.locale.timezone}")
  private String timeZone;

  public UserKYCServiceImpl(
      PersonService personService,
      RequestStateService requestStateService,
      CompanyService companyService,
      EmailService emailService,
      UserKYCDocumentService userKYCDocumentService,
      UserRoleService userRoleService,
      UserKYCRepository userKYCRepository,
      BlockchainMessageProducer blockchainMessageProducer,
      NotificationService notificationService) {
    this.personService = personService;
    this.requestStateService = requestStateService;
    this.companyService = companyService;
    this.emailService = emailService;
    this.userKYCDocumentService = userKYCDocumentService;
    this.userRoleService = userRoleService;
    this.userKYCRepository = userKYCRepository;
    this.blockchainMessageProducer = blockchainMessageProducer;
    this.notificationService = notificationService;
  }

  @Override
  @Transactional
  public UserKYC submitUserKYCRequest(UserKYCRequestDto userKYCRequestDto) {
    throwIfHasPendingKYC();

    UserKYC userKYC =
        UserKYC.builder()
            .user(AuthenticationUtil.getAuthentication().getAuth())
            .requestState(requestStateService.getRequestState(RequestStateName.PENDING))
            .uploadDate(Instant.now())
            .politicallyExposed(userKYCRequestDto.isPoliticallyExposed())
            .build();

    userKYC = userKYCRepository.save(userKYC);

    userKYC = userKYCDocumentService.submitDocuments(userKYC, userKYCRequestDto.getDocuments());

    blockchainMessageProducer.produceMessage(
        BlockchainMethod.USER_KYC_REQUEST_FOR_REVIEW,
        new KYCRequestForReviewDto(
            userKYC, AuthenticationUtil.getAuthentication().getAuth().getId()),
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

    blockchainMessageProducer.produceMessage(
        BlockchainMethod.USER_KYC_STATE_CHANGE,
        new KYCChangeStateDto(userKYC, AuthenticationUtil.getAuthentication().getAuth().getId()),
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
  public UserKYCResponseWithFilesDto getUserKYC() {
    UserKYC userKYC = findLastKYCForCurrentUser();

    if (userKYC == null) {
      return null;
    }
    return convertUserKYCToUserKYCResponseWithFilesDto(userKYC);
  }

  @Override
  public UserKYCResponseWithFilesDto getUserKYC(Long userKYCId) {
    UserKYC userKYC = findByIdOrThrowException(userKYCId);
    throwIfNoAccess(userKYC);

    return convertUserKYCToUserKYCResponseWithFilesDto(userKYC);
  }

  @Override
  public Page<UserKYCResponseDto> getUserKYCs(Pageable pageable, String requestState, String role) {
    UserRole userRole = (role == null) ? null : userRoleService.getUserRole(role.toUpperCase());
    RequestState state = getRequestStateFromFilterParameter(requestState);
    Boolean isAssigned = getAssignedFlag(requestState);
    return userKYCRepository
        .findAllByRequestStateAndByUserRoleAndByAssigned(pageable, state, userRole, isAssigned)
        .map(this::convertUserKYCToUserKYCResponseDto);
  }

  @Override
  public UserKYC approveUserKYC(Long userKYCId) {
    UserKYC userKYC = findByIdOrThrowException(userKYCId);
    throwIfNotPending(userKYC);
    throwIfNotAuditor(userKYC);
    userKYC.setRequestState(requestStateService.getRequestState(RequestStateName.APPROVED));
    userKYC = userKYCRepository.save(userKYC);
    sendKYCApprovalEmail(userKYC);
    Auth recipient = userKYC.getUser();
    notificationService.sendMessage(recipient, NotificationType.KYC_APPROVAL, null);

    blockchainMessageProducer.produceMessage(
        BlockchainMethod.USER_KYC_STATE_CHANGE,
        new KYCChangeStateDto(userKYC, AuthenticationUtil.getAuthentication().getAuth().getId()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());

    return userKYC;
  }

  private Boolean getAssignedFlag(String requestState) {
    if (requestState == null) {
      return null;
    } else {
      return !requestState.toUpperCase().endsWith("NOTASSIGNED");
    }
  }

  private RequestState getRequestStateFromFilterParameter(String requestState) {
    if (requestState == null) {
      return null;
    } else {
      requestState = requestState.toUpperCase();
      if (requestState.startsWith(RequestStateName.PENDING.toString())) {
        return requestStateService.getRequestState(RequestStateName.PENDING);
      } else {
        return requestStateService.getRequestState(RequestStateName.valueOf(requestState));
      }
    }
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
    Auth recipient = userKYC.getUser();
    notificationService.sendMessage(recipient, NotificationType.KYC_REJECTION, rejectionReason);

    blockchainMessageProducer.produceMessage(
        BlockchainMethod.USER_KYC_STATE_CHANGE,
        new KYCChangeStateDto(userKYC, AuthenticationUtil.getAuthentication().getAuth().getId()),
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
            || UserRoleName.getInvestorRoleNames().contains(userRoleName))
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

  private void throwIfHasPendingKYC() {
    UserKYC userKYC = findLastKYCForCurrentUser();
    if (userKYC != null && userKYC.getRequestState().getName().equals(RequestStateName.PENDING)) {
      throw new BadRequestException(USER_HAS_PENDING_KYC);
    }
  }

  private UserKYC findByIdOrThrowException(Long id) {
    return userKYCRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException(USER_KYC_DOES_NOT_EXIST));
  }

  private UserKYC findLastKYCForCurrentUser() {
    Auth auth = AuthenticationUtil.getAuthentication().getAuth();

    return userKYCRepository.findFirstByUserOrderByUploadDateDesc(auth).orElse(null);
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
