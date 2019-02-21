package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.api.dto.enums.E2FAStatus;
import io.realmarket.propeler.api.dto.enums.EEmailType;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.AuthorizedAction;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.model.enums.EAuthState;
import io.realmarket.propeler.model.enums.EAuthorizationActionType;
import io.realmarket.propeler.model.enums.ETemporaryTokenType;
import io.realmarket.propeler.model.enums.EUserRole;
import io.realmarket.propeler.repository.AuthRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.exception.ForbiddenRoleException;
import io.realmarket.propeler.service.exception.UsernameAlreadyExistsException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.MailContentHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.INVALID_REQUEST;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

  public static final Long EMAIL_CHANGE_ACTION_MILLISECONDS = 300000L;

  public static final Long PASSWORD_CHANGE_ACTION_MILLISECONDS = 180000L;

  private static final List<EUserRole> ALLOWED_ROLES =
      Arrays.asList(EUserRole.ROLE_ENTREPRENEUR, EUserRole.ROLE_INVESTOR);

  private final AuthRepository authRepository;

  private final PersonService personService;
  private final EmailService emailService;
  private final TemporaryTokenService temporaryTokenService;
  private final JWTService jwtService;
  private final AuthorizedActionService authorizedActionService;

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public AuthServiceImpl(
      PasswordEncoder passwordEncoder,
      PersonService personService,
      EmailService emailService,
      AuthRepository authRepository,
      TemporaryTokenService temporaryTokenService,
      JWTService jwtService,
      AuthorizedActionService authorizedActionService) {
    this.passwordEncoder = passwordEncoder;
    this.personService = personService;
    this.emailService = emailService;
    this.authRepository = authRepository;
    this.temporaryTokenService = temporaryTokenService;
    this.jwtService = jwtService;
    this.authorizedActionService = authorizedActionService;
  }

  public static Collection<? extends GrantedAuthority> getAuthorities(EUserRole userRole) {
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(userRole.toString()));
    return authorities;
  }

  public AuthResponseDto login(LoginDto loginDto) {
    Auth auth =
        authRepository
            .findByUsername(loginDto.getUsername())
            .orElseThrow(
                () -> new BadCredentialsException(ExceptionMessages.INVALID_CREDENTIALS_MESSAGE));
    return validateLogin(auth, loginDto.getPassword());
  }

  @Transactional
  public void register(RegistrationDto registrationDto) {
    if (authRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
      log.error(
          "User with the provided username '{}' already exists!", registrationDto.getUsername());
      throw new UsernameAlreadyExistsException(ExceptionMessages.USERNAME_ALREADY_EXISTS);
    }

    if (!isRoleAllowed(registrationDto.getUserRole())) {
      throw new ForbiddenRoleException(INVALID_REQUEST);
    }

    Person person = this.personService.save(new Person(registrationDto));

    Auth auth =
        this.authRepository.save(
            Auth.builder()
                .username(registrationDto.getUsername())
                .state(EAuthState.CONFIRM_REGISTRATION)
                .userRole(registrationDto.getUserRole())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .person(person)
                .build());

    TemporaryToken temporaryToken =
        temporaryTokenService.createToken(auth, ETemporaryTokenType.REGISTRATION_TOKEN);

    emailService.sendMailToUser(
        new MailContentHolder(
            registrationDto.getEmail(),
            EEmailType.REGISTER,
            Collections.unmodifiableMap(
                Stream.of(
                        new SimpleEntry<>(EmailServiceImpl.USERNAME, registrationDto.getUsername()),
                        new SimpleEntry<>(
                            EmailServiceImpl.ACTIVATION_TOKEN, temporaryToken.getValue()))
                    .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)))));

    log.info("User with username '{}' saved successfully.", registrationDto.getUsername());
  }

  @Transactional
  public void updateSecretById(Long id, String secret) {
    Auth auth = findByIdOrThrowException(id);
    auth.setTotpSecret(secret);
    authRepository.save(auth);
  }

  @Override
  public void finalize2faInitialization(Auth auth) {
    auth.setState(EAuthState.ACTIVE);
    authRepository.save(auth);
  }

  public String findSecretById(Long id) {
    return findByIdOrThrowException(id).getTotpSecret();
  }

  public void confirmRegistration(ConfirmRegistrationDto confirmRegistrationDto) {
    TemporaryToken temporaryToken =
        temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            confirmRegistrationDto.getToken());

    Auth auth = temporaryToken.getAuth();
    auth.setState(EAuthState.INITIALIZE_2FA);
    authRepository.save(auth);

    temporaryTokenService.deleteToken(temporaryToken);
  }

  public void recoverUsername(EmailDto emailDto) {
    List<Person> personList = personService.findByEmail(emailDto.getEmail());

    if (personList.isEmpty()) {
      throw new EntityNotFoundException(ExceptionMessages.EMAIL_DOES_NOT_EXIST);
    }
    List<String> usernameList =
        personList.stream().map(p -> p.getAuth().getUsername()).collect(Collectors.toList());

    emailService.sendMailToUser(
        new MailContentHolder(
            emailDto.getEmail(),
            EEmailType.RECOVER_USERNAME,
            Collections.singletonMap(EmailServiceImpl.USERNAME_LIST, usernameList)));
  }

  public void initializeResetPassword(UsernameDto usernameDto) {
    Auth auth = findByUsernameOrThrowException(usernameDto.getUsername());

    TemporaryToken temporaryToken =
        temporaryTokenService.createToken(auth, ETemporaryTokenType.RESET_PASSWORD_TOKEN);

    emailService.sendMailToUser(
        new MailContentHolder(
            auth.getPerson().getEmail(),
            EEmailType.RESET_PASSWORD,
            Collections.singletonMap(EmailServiceImpl.RESET_TOKEN, temporaryToken.getValue())));
  }

  @Transactional
  public void finalizeResetPassword(ResetPasswordDto resetPasswordDto) {
    TemporaryToken temporaryToken =
        temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            resetPasswordDto.getResetPasswordToken());

    Auth auth = temporaryToken.getAuth();

    jwtService.deleteAllByAuth(auth);
    temporaryTokenService.deleteToken(temporaryToken);

    auth.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
    authRepository.save(auth);
  }

  @Transactional
  public void logout() {
    jwtService.deleteByValue(AuthenticationUtil.getAuthentication().getToken());
  }

  @Transactional
  @Override
  public void initializeChangePassword(Long authId, ChangePasswordDto changePasswordDto) {
    checkIfAllowed(authId);

    Auth auth = findByIdOrThrowException(authId);
    if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), auth.getPassword())) {
      throw new BadCredentialsException(ExceptionMessages.INVALID_CREDENTIALS_MESSAGE);
    }

    authorizedActionService.storeAuthorizationAction(
        auth.getId(),
        EAuthorizationActionType.NEW_PASSWORD,
        passwordEncoder.encode(changePasswordDto.getNewPassword()),
        PASSWORD_CHANGE_ACTION_MILLISECONDS);
  }

  @Transactional
  public void finalizeChangePassword(Long authId, TwoFADto twoFACodeDto) {
    checkIfAllowed(authId);
    Auth auth = findByIdOrThrowException(authId);

    final String newPassword =
        authorizedActionService
            .validateAuthorizationAction(auth, EAuthorizationActionType.NEW_PASSWORD, twoFACodeDto)
            .orElseThrow(() -> new ForbiddenOperationException(INVALID_REQUEST));

    auth.setPassword(newPassword);
    auth = authRepository.save(auth);

    authorizedActionService.deleteByAuthAndType(auth, EAuthorizationActionType.NEW_PASSWORD);
    jwtService.deleteAllByAuthAndValueNot(auth, AuthenticationUtil.getAuthentication().getToken());
  }

  @Override
  public Optional<Auth> findById(Long id) {
    return authRepository.findById(id);
  }

  public Auth findByUsernameOrThrowException(String username) {
    return authRepository
        .findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.USERNAME_DOES_NOT_EXISTS));
  }

  public Auth findByUserIdrThrowException(Long userId) {
    return authRepository
        .findByPersonId(userId)
        .orElseThrow(
            () -> new EntityNotFoundException(ExceptionMessages.PERSON_ID_DOES_NOT_EXISTS));
  }

  public Auth findByIdOrThrowException(Long id) {
    return authRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.USERNAME_DOES_NOT_EXISTS));
  }

  public void initializeEmailChange(final Long authId, final EmailDto emaildto) {
    checkIfAllowed(authId);
    authorizedActionService.storeAuthorizationAction(
        authId,
        EAuthorizationActionType.NEW_EMAIL,
        emaildto.getEmail(),
        EMAIL_CHANGE_ACTION_MILLISECONDS);
  }

  @Transactional
  public void verifyEmailChangeRequest(final Long authId, final TwoFADto twoFACodeDto) {
    checkIfAllowed(authId);
    final Auth auth = findByIdOrThrowException(authId);
    final String newEmail =
        authorizedActionService
            .validateAuthorizationAction(auth, EAuthorizationActionType.NEW_EMAIL, twoFACodeDto)
            .orElseThrow(() -> new ForbiddenOperationException(ExceptionMessages.INVALID_REQUEST));
    final TemporaryToken token =
        temporaryTokenService.createToken(auth, ETemporaryTokenType.EMAIL_CHANGE_TOKEN);

    log.info("Token for change email {}", token.getValue());

    emailService.sendMailToUser(
        new MailContentHolder(
            newEmail,
            EEmailType.CHANGE_EMAIL,
            Collections.singletonMap(EmailServiceImpl.EMAIL_CHANGE_TOKEN, token.getValue())));
  }

  @Transactional
  public void finalizeEmailChange(final ConfirmEmailChangeDto confirmEmailChangeDto) {
    final TemporaryToken token =
        temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            confirmEmailChangeDto.getToken());
    final Auth currentAuth = token.getAuth();
    final AuthorizedAction authorizedAction =
        authorizedActionService.findAuthorizedActionOrThrowException(
            currentAuth, EAuthorizationActionType.NEW_EMAIL);
    changePersonEmail(token, authorizedAction);
    temporaryTokenService.deleteToken(token);
    authorizedActionService.deleteByAuthAndType(
        authorizedAction.getAuth(), authorizedAction.getType());
  }

  private Boolean isRoleAllowed(EUserRole role) {
    return ALLOWED_ROLES.contains(role);
  }

  private AuthResponseDto validateLogin(Auth auth, String password) {
    checkLoginCredentials(auth, password);
    if (auth.getState().equals(EAuthState.INITIALIZE_2FA)) {
      return new AuthResponseDto(
          E2FAStatus.INITIALIZE,
          temporaryTokenService.createToken(auth, ETemporaryTokenType.SETUP_2FA_TOKEN).getValue());
    }
    return new AuthResponseDto(
        E2FAStatus.VALIDATE,
        temporaryTokenService.createToken(auth, ETemporaryTokenType.LOGIN_TOKEN).getValue());
  }

  public void checkLoginCredentials(Auth auth, String password) {
    if (auth.getState().equals(EAuthState.CONFIRM_REGISTRATION)) {

      log.error("User with auth id '{}' is not active ", auth.getId());
      throw new BadCredentialsException(ExceptionMessages.INVALID_CREDENTIALS_MESSAGE);
    }
    if (!passwordEncoder.matches(password, auth.getPassword())) {
      log.error("User with auth id '{}' provided passwords which do not match ", auth.getId());
      throw new BadCredentialsException(ExceptionMessages.INVALID_CREDENTIALS_MESSAGE);
    }
  }

  private void changePersonEmail(
      final TemporaryToken token, final AuthorizedAction changeEmailAction) {
    Person person = token.getAuth().getPerson();
    person.setEmail(changeEmailAction.getData());
    personService.save(person);
  }

  private void checkIfAllowed(final Long authIdFromRequestPath) {
    if (!authIdFromRequestPath.equals(AuthenticationUtil.getAuthentication().getAuth().getId())) {
      throw new ForbiddenOperationException(ExceptionMessages.FORBIDDEN_OPERATION_EXCEPTION);
    }
  }
}
