package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.DocumentResponseDto;
import io.realmarket.propeler.api.dto.UserKYCAssignmentDto;
import io.realmarket.propeler.api.dto.UserKYCResponseDto;
import io.realmarket.propeler.api.dto.UserKYCResponseWithFilesDto;
import io.realmarket.propeler.model.*;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.UserKYCRepository;
import io.realmarket.propeler.repository.UserRoleRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.*;

@Service
public class UserKYCServiceImpl implements UserKYCService {

  private final PersonService personService;
  private final RequestStateService requestStateService;
  private final UserKYCRepository userKYCRepository;
  private final AuthService authService;
  private final CompanyService companyService;
  private final PersonDocumentService personDocumentService;
  private final UserRoleRepository userRoleRepository;

  public UserKYCServiceImpl(
      PersonService personService,
      RequestStateService requestStateService,
      UserKYCRepository userKYCRepository,
      AuthService authService,
      CompanyService companyService,
      PersonDocumentService personDocumentService,
      UserRoleRepository userRoleRepository) {
    this.personService = personService;
    this.requestStateService = requestStateService;
    this.userKYCRepository = userKYCRepository;
    this.authService = authService;
    this.companyService = companyService;
    this.personDocumentService = personDocumentService;
    this.userRoleRepository = userRoleRepository;
  }

  @Override
  public UserKYC createUserKYCRequest() {
    UserKYC userKYC =
        UserKYC.builder()
            .user(AuthenticationUtil.getAuthentication().getAuth())
            .requestState(requestStateService.getRequestState(RequestStateName.PENDING))
            .build();

    return userKYCRepository.save(userKYC);
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
    userKYC.setAuditor(auditorAuth);
    return userKYCRepository.save(userKYC);
  }

  @Override
  public UserKYCResponseWithFilesDto getUserKYC(Long userKYCId) {
    return convertUserKYCToUserKYCResponseWithFilesDto(findByIdOrThrowException(userKYCId));
  }

  @Override
  public Page<UserKYCResponseDto> getUserKYCs(Pageable pageable, String requestState, String role) {
    boolean isRoleAll = role.equals("all");
    boolean isStateAll = requestState.equals("all");
    Page<UserKYC> results = null;
    if (isStateAll && isRoleAll) results = userKYCRepository.findAll(pageable);
    else if (isStateAll) {
      UserRole userRole = getUserRole(role);
      results = userKYCRepository.findAllByUserRole(pageable, userRole);
    } else if (isRoleAll) {
      RequestState state = null;
      if (requestState.toLowerCase().startsWith("pending")) state = getUserKYCState("PENDING");
      else state = getUserKYCState(requestState);
      if (requestState.toLowerCase().endsWith("notassigned"))
        results = userKYCRepository.findAllByRequestStateNotAssigned(pageable, state);
      else results = userKYCRepository.findAllByRequestStateAssigned(pageable, state);
    } else {
      UserRole userRole = getUserRole(role);
      RequestState state = null;
      if (requestState.toLowerCase().startsWith("pending")) state = getUserKYCState("PENDING");
      else state = getUserKYCState(requestState);
      if (requestState.toLowerCase().endsWith("notassigned"))
        results =
            userKYCRepository.findAllByRequestStateAndByUserRoleNotAssigned(
                pageable, state, userRole);
      else
        results =
            userKYCRepository.findAllByRequestStateAndByUserRoleAssigned(
                pageable, state, userRole);
    }
    return results.map(this::convertUserKYCToUserKYCResponseDto);
  }

  @Override
  public UserKYC approveUserKYC(Long userKYCId) {
    UserKYC userKYC = findByIdOrThrowException(userKYCId);
    throwIfNotAuditor(userKYC);
    userKYC.setRequestState(requestStateService.getRequestState(RequestStateName.APPROVED));
    return userKYCRepository.save(userKYC);
  }

  @Override
  public UserKYC rejectUserKYC(Long userKYCId, String content) {
    UserKYC userKYC = findByIdOrThrowException(userKYCId);
    throwIfNotAuditor(userKYC);
    userKYC.setRequestState(requestStateService.getRequestState(RequestStateName.DECLINED));
    userKYC.setContent(content);
    return userKYCRepository.save(userKYC);
  }

  private UserRole getUserRole(String role) {
    return userRoleRepository.findByName(getUserRoleNameOrThrow(role)).get();
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
    if (user.getUserRole().getName().equals(UserRoleName.ROLE_ENTREPRENEUR))
      company = companyService.findByAuthIdOrThrowException(user.getId());
    List<PersonDocument> userDocuments = personDocumentService.findByPerson(person);
    return new UserKYCResponseWithFilesDto(
        userKYC,
        person,
        user,
        company,
        userDocuments.stream().map(DocumentResponseDto::new).collect(Collectors.toList()));
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
