package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.UserKYCRepository;
import io.realmarket.propeler.service.PersonService;
import io.realmarket.propeler.service.RequestStateService;
import io.realmarket.propeler.service.UserKYCService;
import org.springframework.stereotype.Service;

@Service
public class UserKYCServiceImpl implements UserKYCService {

  private final PersonService personService;
  private final RequestStateService requestStateService;
  private final UserKYCRepository userKYCRepository;

  public UserKYCServiceImpl(
      PersonService personService,
      RequestStateService requestStateService,
      UserKYCRepository userKYCRepository) {
    this.personService = personService;
    this.requestStateService = requestStateService;
    this.userKYCRepository = userKYCRepository;
  }

  @Override
  public UserKYC createUserKYCRequest(Long personId) {

    UserKYC userKYC =
        UserKYC.builder()
            .person(personService.findByIdOrThrowException(personId))
            .requestState(requestStateService.getRequestState(RequestStateName.PENDING))
            .build();

    return userKYCRepository.save(userKYC);
  }
}
