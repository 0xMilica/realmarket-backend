package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.api.dto.enums.E2FAStatus;
import io.realmarket.propeler.api.dto.enums.EmailType;
import io.realmarket.propeler.model.*;
import io.realmarket.propeler.model.enums.AuthStateName;
import io.realmarket.propeler.model.enums.AuthorizedActionTypeName;
import io.realmarket.propeler.model.enums.TemporaryTokenTypeName;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.AuthRepository;
import io.realmarket.propeler.repository.CountryRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.blockchain.BlockchainCommunicationService;
import io.realmarket.propeler.service.blockchain.BlockchainMethod;
import io.realmarket.propeler.service.blockchain.dto.user.EmailChangeDto;
import io.realmarket.propeler.service.blockchain.dto.user.PasswordChangeDto;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.exception.UsernameAlreadyExistsException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.*;
import liquibase.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.realmarket.propeler.model.enums.TemporaryTokenTypeName.PASSWORD_VERIFIED_TOKEN;
import static io.realmarket.propeler.service.exception.util.ExceptionMessages.INVALID_COUNTRY_CODE;
import static io.realmarket.propeler.service.exception.util.ExceptionMessages.INVALID_REQUEST;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

  public static final Long EMAIL_CHANGE_ACTION_MILLISECONDS = 300000L;

  public static final Long PASSWORD_CHANGE_ACTION_MILLISECONDS = 180000L;

  private final AuthRepository authRepository;
  private final CountryRepository countryRepository;

  private final AuthStateService authStateService;
  private final UserRoleService userRoleService;
  private final PersonService personService;
  private final EmailService emailService;
  private final TemporaryTokenService temporaryTokenService;
  private final RegistrationTokenService registrationTokenService;
  private final JWTService jwtService;
  private final AuthorizedActionService authorizedActionService;
  private final LoginIPAttemptsService loginIPAttemptsService;
  private final LoginUsernameAttemptsService loginUsernameAttemptsService;
  private final PasswordEncoder passwordEncoder;

  private final RememberMeCookieService rememberMeCookieService;
  private final BlockchainCommunicationService blockchainCommunicationService;

  @Autowired
  public AuthServiceImpl(
      PasswordEncoder passwordEncoder,
      PersonService personService,
      EmailService emailService,
      AuthRepository authRepository,
      UserRoleService userRoleService,
      AuthStateService authStateService,
      CountryRepository countryRepository,
      TemporaryTokenService temporaryTokenService,
      RegistrationTokenService registrationTokenService,
      RememberMeCookieService rememberMeCookieService,
      JWTService jwtService,
      AuthorizedActionService authorizedActionService,
      LoginIPAttemptsService loginAttemptsService,
      LoginUsernameAttemptsService loginUsernameAttemptsService,
      BlockchainCommunicationService blockchainCommunicationService) {
    this.passwordEncoder = passwordEncoder;
    this.personService = personService;
    this.emailService = emailService;
    this.authRepository = authRepository;
    this.userRoleService = userRoleService;
    this.authStateService = authStateService;
    this.countryRepository = countryRepository;
    this.temporaryTokenService = temporaryTokenService;
    this.registrationTokenService = registrationTokenService;
    this.jwtService = jwtService;
    this.rememberMeCookieService = rememberMeCookieService;
    this.authorizedActionService = authorizedActionService;
    this.loginIPAttemptsService = loginAttemptsService;
    this.loginUsernameAttemptsService = loginUsernameAttemptsService;
    this.blockchainCommunicationService = blockchainCommunicationService;
  }

  public static Collection<? extends GrantedAuthority> getAuthorities(UserRoleName userRole) {
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(userRole.toString()));
    return authorities;
  }

  public AuthResponseDto login(LoginDto loginDto, HttpServletRequest request) {
    Optional<Auth> authOptional = authRepository.findByUsername(loginDto.getUsername());
    if (!authOptional.isPresent()) {
      loginIPAttemptsService.loginFailed(AuthenticationUtil.getClientIp());
      throw new BadCredentialsException(ExceptionMessages.INVALID_CREDENTIALS_MESSAGE);
    }
    if (authOptional.get().getBlocked()) {
      log.info("Username blocked");
      throw new ForbiddenOperationException(ExceptionMessages.BLOCKED_CLIENT);
    }
    return checkIfRemembered(
        validateLogin(authOptional.get(), loginDto.getPassword()), authOptional.get(), request);
  }

  @Transactional
  public void registerEntrepreneur(EntrepreneurRegistrationDto entrepreneurRegistrationDto) {
    RegistrationToken registrationToken =
        registrationTokenService.findByValueAndNotExpiredOrThrowException(
            entrepreneurRegistrationDto.getRegistrationToken());

    if (!registrationToken
            .getFundraisingProposal()
            .getFirstName()
            .equals(entrepreneurRegistrationDto.getFirstName())
        || !registrationToken
            .getFundraisingProposal()
            .getLastName()
            .equals(entrepreneurRegistrationDto.getLastName())) {
      throw new BadRequestException(INVALID_REQUEST);
    }

    register(entrepreneurRegistrationDto, UserRoleName.ROLE_ENTREPRENEUR);
    registrationTokenService.deleteToken(registrationToken);
  }

  @Transactional
  public void register(RegistrationDto registrationDto, UserRoleName userRoleName) {
    if (authRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
      log.error(
          "User with the provided username '{}' already exists!", registrationDto.getUsername());
      throw new UsernameAlreadyExistsException(ExceptionMessages.USERNAME_ALREADY_EXISTS);
    }

    UserRole userRole = userRoleService.getUserRole(userRoleName);
    AuthState authState = this.authStateService.getAuthState(AuthStateName.CONFIRM_REGISTRATION);

    Country countryOfResidence =
        findCountryByCodeOrThrowException(registrationDto.getCountryOfResidence());

    Country countryForTaxation =
        findCountryByCodeOrReturnNull(registrationDto.getCountryForTaxation());

    Person person =
        this.personService.save(
            new Person(registrationDto, countryOfResidence, countryForTaxation));

    Auth auth =
        this.authRepository.save(
            Auth.builder()
                .username(registrationDto.getUsername())
                .state(authState)
                .userRole(userRole)
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .person(person)
                .blocked(false)
                .build());

    createTokenAndSendMail(auth, registrationDto);
  }

  public RegistrationTokenInfoDto validateToken(String tokenValue) {
    RegistrationToken registrationToken =
        registrationTokenService.findByValueAndNotExpiredOrThrowException(tokenValue);
    return new RegistrationTokenInfoDto(
        registrationToken.getFundraisingProposal().getFirstName(),
        registrationToken.getFundraisingProposal().getLastName());
  }

  private Country findCountryByCodeOrThrowException(String code) {
    return this.countryRepository
        .findByCode(code)
        .orElseThrow(() -> new BadRequestException(INVALID_COUNTRY_CODE));
  }

  private Country findCountryByCodeOrReturnNull(String code) {
    return (StringUtils.isNotEmpty(code))
        ? this.countryRepository
            .findByCode(code)
            .orElseThrow(() -> new BadRequestException(INVALID_COUNTRY_CODE))
        : null;
  }

  private void createTokenAndSendMail(Auth auth, RegistrationDto registrationDto) {
    TemporaryToken temporaryToken =
        temporaryTokenService.createToken(auth, TemporaryTokenTypeName.REGISTRATION_TOKEN);

    emailService.sendMailToUser(
        new MailContentHolder(
            Collections.singletonList(registrationDto.getEmail()),
            EmailType.REGISTER,
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
    AuthState authState = this.authStateService.getAuthState(AuthStateName.ACTIVE);

    auth.setState(authState);
    authRepository.save(auth);
  }

  public String findSecretById(Long id) {
    return findByIdOrThrowException(id).getTotpSecret();
  }

  public void confirmRegistration(ConfirmRegistrationDto confirmRegistrationDto) {
    TemporaryToken temporaryToken =
        temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            confirmRegistrationDto.getToken());

    AuthState authState = this.authStateService.getAuthState(AuthStateName.INITIALIZE_2FA);

    Auth auth = temporaryToken.getAuth();
    auth.setState(authState);
    authRepository.save(auth);

    temporaryTokenService.deleteToken(temporaryToken);

    blockchainCommunicationService.invoke(
        BlockchainMethod.USER_REGISTRATION,
        new io.realmarket.propeler.service.blockchain.dto.user.RegistrationDto(auth),
        auth.getUsername(),
        AuthenticationUtil.getClientIp());
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
            Collections.singletonList(emailDto.getEmail()),
            EmailType.RECOVER_USERNAME,
            Collections.singletonMap(EmailServiceImpl.USERNAME_LIST, usernameList)));
  }

  public void initializeResetPassword(UsernameDto usernameDto) {
    Auth auth = findByUsernameOrThrowException(usernameDto.getUsername());

    TemporaryToken temporaryToken =
        temporaryTokenService.createToken(auth, TemporaryTokenTypeName.RESET_PASSWORD_TOKEN);

    emailService.sendMailToUser(
        new MailContentHolder(
            Collections.singletonList(auth.getPerson().getEmail()),
            EmailType.RESET_PASSWORD,
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
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    jwtService.deleteByValue(AuthenticationUtil.getAuthentication().getToken());
    rememberMeCookieService.deleteCurrentCookie(request, response);
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
        AuthorizedActionTypeName.NEW_PASSWORD,
        passwordEncoder.encode(changePasswordDto.getNewPassword()),
        PASSWORD_CHANGE_ACTION_MILLISECONDS);
  }

  @Transactional
  public void finalizeChangePassword(Long authId, TwoFADto twoFACodeDto) {
    checkIfAllowed(authId);
    Auth auth = findByIdOrThrowException(authId);

    final String newPassword =
        authorizedActionService
            .validateAuthorizationAction(auth, AuthorizedActionTypeName.NEW_PASSWORD, twoFACodeDto)
            .orElseThrow(() -> new ForbiddenOperationException(INVALID_REQUEST));

    auth.setPassword(newPassword);
    auth = authRepository.save(auth);

    authorizedActionService.deleteByAuthAndType(auth, AuthorizedActionTypeName.NEW_PASSWORD);
    jwtService.deleteAllByAuthAndValueNot(auth, AuthenticationUtil.getAuthentication().getToken());

    blockchainCommunicationService.invoke(
        BlockchainMethod.USER_PASSWORD_CHANGE,
        PasswordChangeDto.builder().userId(authId).build(),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());
  }

  @Override
  public TokenDto verifyPasswordAndReturnToken(Long authId, PasswordDto passwordDto) {
    Auth auth = findByIdOrThrowException(authId);
    checkPasswordOrThrow(auth, passwordDto.getPassword());
    return new TokenDto(
        temporaryTokenService.createToken(auth, PASSWORD_VERIFIED_TOKEN).getValue());
  }

  @Override
  public Optional<Auth> findById(Long id) {
    return authRepository.findById(id);
  }

  public Auth findByUsernameOrThrowException(String username) {
    return authRepository
        .findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.USERNAME_DOES_NOT_EXIST));
  }

  public Auth findByUserIdrThrowException(Long userId) {
    return authRepository
        .findByPersonId(userId)
        .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.PERSON_ID_DOES_NOT_EXIST));
  }

  public Auth findByIdOrThrowException(Long id) {
    return authRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.USERNAME_DOES_NOT_EXIST));
  }

  public List<Auth> findAllInvestors() {
    List<Auth> investors = new ArrayList<>();
    for (UserRoleName investor : UserRoleName.getInvestorRoleNames()) {
      investors.addAll(authRepository.findAllByUserRoleName(investor));
    }
    return investors;
  }

  public void initializeEmailChange(final Long authId, final EmailDto emailDto) {
    checkIfAllowed(authId);
    authorizedActionService.storeAuthorizationAction(
        authId,
        AuthorizedActionTypeName.NEW_EMAIL,
        emailDto.getEmail(),
        EMAIL_CHANGE_ACTION_MILLISECONDS);
  }

  @Transactional
  public void verifyEmailChangeRequest(final Long authId, final TwoFADto twoFACodeDto) {
    checkIfAllowed(authId);
    final Auth auth = findByIdOrThrowException(authId);
    final String newEmail =
        authorizedActionService
            .validateAuthorizationAction(auth, AuthorizedActionTypeName.NEW_EMAIL, twoFACodeDto)
            .orElseThrow(() -> new ForbiddenOperationException(ExceptionMessages.INVALID_REQUEST));
    final TemporaryToken token =
        temporaryTokenService.createToken(auth, TemporaryTokenTypeName.EMAIL_CHANGE_TOKEN);

    log.info("Token for change email {}", token.getValue());

    emailService.sendMailToUser(
        new MailContentHolder(
            Collections.singletonList(newEmail),
            EmailType.CHANGE_EMAIL,
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
            currentAuth, AuthorizedActionTypeName.NEW_EMAIL);
    changePersonEmail(token, authorizedAction);
    temporaryTokenService.deleteToken(token);
    authorizedActionService.deleteByAuthAndType(
        authorizedAction.getAuth(), authorizedAction.getType().getName());

    blockchainCommunicationService.invoke(
        BlockchainMethod.USER_EMAIL_CHANGE,
        EmailChangeDto.builder()
            .userId(currentAuth.getId())
            .newEmailHash(HashingHelper.hash(authorizedAction.getData()))
            .build(),
        currentAuth.getUsername(),
        AuthenticationUtil.getClientIp());
  }

  private AuthResponseDto validateLogin(Auth auth, String password) {
    checkLoginCredentials(auth, password);
    loginIPAttemptsService.loginSucceeded(AuthenticationUtil.getClientIp());
    loginUsernameAttemptsService.loginSucceeded(auth.getUsername());
    if (auth.getState().getName().equals(AuthStateName.INITIALIZE_2FA)) {
      return new AuthResponseDto(
          E2FAStatus.INITIALIZE,
          temporaryTokenService
              .createToken(auth, TemporaryTokenTypeName.SETUP_2FA_TOKEN)
              .getValue());
    }
    return new AuthResponseDto(
        E2FAStatus.VALIDATE,
        temporaryTokenService.createToken(auth, TemporaryTokenTypeName.LOGIN_TOKEN).getValue());
  }

  public void checkLoginCredentials(Auth auth, String password) {
    if (auth.getState().getName().equals(AuthStateName.CONFIRM_REGISTRATION)) {
      log.error("User with auth id '{}' is not active ", auth.getId());
      loginIPAttemptsService.loginFailed(AuthenticationUtil.getClientIp());
      blockByUsernameIfNotBlocked(auth);
      throw new BadCredentialsException(ExceptionMessages.INVALID_CREDENTIALS_MESSAGE);
    }
    checkPasswordOrThrow(auth, password);
  }

  private void checkPasswordOrThrow(Auth auth, String password) {
    if (!passwordEncoder.matches(password, auth.getPassword())) {
      log.error("User with auth id '{}' provided passwords which do not match ", auth.getId());
      loginIPAttemptsService.loginFailed(AuthenticationUtil.getClientIp());
      blockByUsernameIfNotBlocked(auth);
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

  private AuthResponseDto checkIfRemembered(
      AuthResponseDto authResponseDto, Auth auth, HttpServletRequest request) {
    if (E2FAStatus.VALIDATE == authResponseDto.getTwoFAStatus()
        && rememberMeCookieService
            .findByValueAndAuthAndNotExpired(RememberMeCookieHelper.getCookieValue(request), auth)
            .isPresent()) {
      authResponseDto.setTwoFAStatus(E2FAStatus.REMEMBER_ME);
    }
    return authResponseDto;
  }

  @Async
  protected void blockByUsernameIfNotBlocked(Auth auth) {
    if (!auth.getBlocked()) {
      loginUsernameAttemptsService.loginFailed(auth.getUsername());
      if (loginUsernameAttemptsService.isBlocked(auth.getUsername())) {
        auth.setBlocked(true);
        authRepository.save(auth);
        emailService.sendMailToUser(
            new MailContentHolder(
                Collections.singletonList(auth.getPerson().getEmail()),
                EmailType.ACCOUNT_BLOCKED,
                Collections.singletonMap(EmailServiceImpl.USERNAME, auth.getUsername())));
      }
    }
  }
}
