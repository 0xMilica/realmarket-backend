package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.UserKYCAssignmentDto;
import io.realmarket.propeler.api.dto.UserKYCResponseDto;
import io.realmarket.propeler.api.dto.UserKYCResponseWithFilesDto;
import io.realmarket.propeler.model.*;
import io.realmarket.propeler.model.enums.DocumentTypeName;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.UserKYCRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.exception.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.USER_CAN_NOT_BE_AUDITOR;
import static io.realmarket.propeler.service.exception.util.ExceptionMessages.USER_KYC_DOES_NOT_EXIST;

@Service
public class UserKYCServiceImpl implements UserKYCService {

  private final PersonService personService;
  private final RequestStateService requestStateService;
  private final UserKYCRepository userKYCRepository;
  private final AuthService authService;
  private final CompanyService companyService;
  private final PersonDocumentService personDocumentService;

  public UserKYCServiceImpl(
      PersonService personService,
      RequestStateService requestStateService,
      UserKYCRepository userKYCRepository,
      AuthService authService,
      CompanyService companyService,
      PersonDocumentService personDocumentService) {
    this.personService = personService;
    this.requestStateService = requestStateService;
    this.userKYCRepository = userKYCRepository;
    this.authService = authService;
    this.companyService = companyService;
    this.personDocumentService = personDocumentService;
  }

  @Override
  public UserKYC createUserKYCRequest() {
    UserKYC userKYC =
        UserKYC.builder()
            .person(
                personService.getPersonFromAuth(AuthenticationUtil.getAuthentication().getAuth()))
            .requestState(requestStateService.getRequestState(RequestStateName.PENDING))
            .build();

    return userKYCRepository.save(userKYC);
  }

  @Override
  public UserKYC assignUserKYC(UserKYCAssignmentDto userKYCAssignmentDto) {
    Auth auditorAuth = authService.findByIdOrThrowException(userKYCAssignmentDto.getAuditorId());
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
  public Page<UserKYCResponseDto> getUserKYCs(Pageable pageable) {
    return userKYCRepository.findAll(pageable).map(this::convertUserKYCToUserKYCResponseDto);
  }

  private UserKYC findByIdOrThrowException(Long id) {
    return userKYCRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException(USER_KYC_DOES_NOT_EXIST));
  }

  private UserKYCResponseWithFilesDto convertUserKYCToUserKYCResponseWithFilesDto(UserKYC userKYC) {
    Person person = userKYC.getPerson();
    Auth auth = person.getAuth();
    Company company = null;
    if (auth.getUserRole().getName().equals(UserRoleName.ROLE_ENTREPRENEUR))
      company = companyService.findByAuthIdOrThrowException(auth.getId());
    String personalIdFrontUrl = null;
    String personalIdBackUrl = null;
    List<PersonDocument> personalId = personDocumentService.findByPerson(person);
    for (PersonDocument pd : personalId) {
      if (pd.getType().getName().equals(DocumentTypeName.PERSONAL_ID_BACK))
        personalIdBackUrl = pd.getUrl();
      if (pd.getType().getName().equals(DocumentTypeName.PERSONAL_ID_FRONT))
        personalIdFrontUrl = pd.getUrl();
    }
    return new UserKYCResponseWithFilesDto(
        userKYC, person, auth, company, personalIdBackUrl, personalIdFrontUrl);
  }

  private UserKYCResponseDto convertUserKYCToUserKYCResponseDto(UserKYC userKYC) {
    Person person = userKYC.getPerson();
    Auth auth = person.getAuth();
    Company company = null;
    if (auth.getUserRole().getName().equals(UserRoleName.ROLE_ENTREPRENEUR))
      company = companyService.findByAuthIdOrThrowException(auth.getId());
    return new UserKYCResponseDto(userKYC, person, auth, company);
  }
}
