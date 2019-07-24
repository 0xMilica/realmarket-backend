package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.UserKYCAssignmentDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.UserKYCRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.PersonService;
import io.realmarket.propeler.service.RequestStateService;
import io.realmarket.propeler.service.UserKYCService;
import io.realmarket.propeler.service.exception.BadRequestException;
import org.springframework.stereotype.Service;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.USER_CAN_NOT_BE_AUDITOR;

@Service
public class UserKYCServiceImpl implements UserKYCService {

  private final PersonService personService;
  private final RequestStateService requestStateService;
  private final UserKYCRepository userKYCRepository;
  private final AuthService authService;

  public UserKYCServiceImpl(
      PersonService personService,
      RequestStateService requestStateService,
      UserKYCRepository userKYCRepository,
      AuthService authService) {
    this.personService = personService;
    this.requestStateService = requestStateService;
    this.userKYCRepository = userKYCRepository;
    this.authService = authService;
  }

  @Override
  public UserKYC createUserKYCRequest() {
    UserKYC userKYC =
        UserKYC.builder()
            .person(personService.getPersonFromAuth(AuthenticationUtil.getAuthentication().getAuth()))
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
}
