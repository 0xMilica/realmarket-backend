package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.ConfirmRegistrationDto;
import io.realmarket.propeler.api.dto.EmailDto;
import io.realmarket.propeler.api.dto.RegistrationDto;
import io.realmarket.propeler.api.dto.enums.EEmailType;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.model.enums.EUserRole;
import io.realmarket.propeler.repository.AuthRepository;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.EmailService;
import io.realmarket.propeler.service.PersonService;
import io.realmarket.propeler.service.exception.ForbiddenRoleException;
import io.realmarket.propeler.service.exception.InvalidTokenException;
import io.realmarket.propeler.service.exception.UsernameAlreadyExistsException;
import io.realmarket.propeler.service.helper.DateTimeHelper;
import io.realmarket.propeler.service.util.TokenValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
  private static final Integer REG_RES_TOKEN_EXPIRATION_TIME = 24;
  private static final List<EUserRole> ALLOWED_ROLES =
      Arrays.asList(EUserRole.ROLE_ENTREPRENEUR, EUserRole.ROLE_INVESTOR);

  private final AuthRepository authRepository;

  private final PersonService personService;
  private final EmailService emailService;

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public AuthServiceImpl(
      PasswordEncoder passwordEncoder,
      PersonService personService,
      EmailService emailService,
      AuthRepository authRepository) {
    this.passwordEncoder = passwordEncoder;
    this.personService = personService;
    this.emailService = emailService;
    this.authRepository = authRepository;
  }

  public void register(RegistrationDto registrationDto) {
    if (authRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
      log.error(
          "User with the provided username '{}' already exists!", registrationDto.getUsername());
      throw new UsernameAlreadyExistsException("User with the provided username already exists!");
    }

    if (!isRoleAllowed(registrationDto.getUserRole())) {
      throw new ForbiddenRoleException("Invalid request!");
    }

    Person person = this.personService.save(new Person(registrationDto));
    Auth auth =
        this.authRepository.save(
            Auth.builder()
                .username(registrationDto.getUsername())
                .active(false)
                .userRole(registrationDto.getUserRole())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .registrationToken(UUID.randomUUID().toString())
                .registrationTokenExpirationTime(
                    DateTimeHelper.getPointInTime(Calendar.HOUR, REG_RES_TOKEN_EXPIRATION_TIME))
                .person(person)
                .build());

    emailService.sendMailToUser(populateEmailDto(registrationDto, auth));

    log.info("User with username '{}' saved successfully.", registrationDto.getUsername());
  }

  public void confirmRegistration(ConfirmRegistrationDto confirmRegistrationDto) {
    Auth auth = findByRegistrationTokenOrThrowException(confirmRegistrationDto.getToken());

    if (TokenValidator.isTokenValid(
        auth.getRegistrationToken(), auth.getRegistrationTokenExpirationTime())) {
      activateUser(auth);
    } else {
      log.error(
          "Invalid token '{}' provided for auth id '{}', expiration time is '{}' '",
          confirmRegistrationDto.getToken(),
          auth.getId(),
          auth.getRegistrationTokenExpirationTime());
      throw new InvalidTokenException("Invalid token provided");
    }
  }

  @Transactional
  @Scheduled(
      fixedRateString = "${app.cleanse.registration.timeloop}",
      initialDelayString = "${app.cleanse.registration.timeloop}")
  public void cleanseFailedRegistrations() {
    log.trace("Clean failed registrations");
    authRepository.deleteByRegistrationTokenExpirationTimeLessThanAndActiveIsFalse(new Date());
  }

  public Auth findByUsernameOrThrowException(String username) {
    return authRepository
        .findByUsername(username)
        .orElseThrow(
            () -> new EntityNotFoundException("User with provided username does not exists."));
  }

  public Auth findByRegistrationTokenOrThrowException(String registrationToken) {
    return authRepository
        .findByRegistrationToken(registrationToken)
        .orElseThrow(() -> new InvalidTokenException("Invalid token provided"));
  }

  private EmailDto populateEmailDto(RegistrationDto registrationDto, Auth auth) {
    EmailDto emailDto = new EmailDto();
    emailDto.setEmail(registrationDto.getEmail());
    emailDto.setType(EEmailType.REGISTER);
    Map<String, Object> parameterMap = new HashMap<>();
    parameterMap.put(EmailServiceImpl.FIRST_NAME, registrationDto.getFirstName());
    parameterMap.put(EmailServiceImpl.LAST_NAME, registrationDto.getLastName());
    parameterMap.put(EmailServiceImpl.ACTIVATION_TOKEN, auth.getRegistrationToken());

    emailDto.setContent(parameterMap);
    return emailDto;
  }

  private Boolean isRoleAllowed(EUserRole role) {
    return ALLOWED_ROLES.contains(role);
  }

  private void activateUser(Auth auth) {
    auth.setActive(true);
    auth.setRegistrationToken(null);
    authRepository.save(auth);
  }
}
