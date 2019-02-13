package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.api.dto.enums.EEmailType;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.EmailChangeRequest;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.model.enums.ETemporaryTokenType;
import io.realmarket.propeler.model.enums.EUserRole;
import io.realmarket.propeler.repository.AuthRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.exception.ForbiddenRoleException;
import io.realmarket.propeler.service.exception.UsernameAlreadyExistsException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.exception.util.ForbiddenOperationException;
import io.realmarket.propeler.service.util.MailContentHolder;
import io.realmarket.propeler.service.util.dto.LoginResponseDto;
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

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

  private static final List<EUserRole> ALLOWED_ROLES =
      Arrays.asList(EUserRole.ROLE_ENTREPRENEUR, EUserRole.ROLE_INVESTOR);

  private final AuthRepository authRepository;

  private final PersonService personService;
  private final EmailService emailService;
  private final TemporaryTokenService temporaryTokenService;
  private final JWTService jwtService;

  private final PasswordEncoder passwordEncoder;

  private final EmailChangeRequestService emailChangeRequestService;

  @Autowired
  public AuthServiceImpl(
      PasswordEncoder passwordEncoder,
      PersonService personService,
      EmailService emailService,
      AuthRepository authRepository,
      TemporaryTokenService temporaryTokenService,
      JWTService jwtService,
      EmailChangeRequestService emailChangeRequestService) {
    this.passwordEncoder = passwordEncoder;
    this.personService = personService;
    this.emailService = emailService;
    this.authRepository = authRepository;
    this.temporaryTokenService = temporaryTokenService;
    this.jwtService = jwtService;
    this.emailChangeRequestService = emailChangeRequestService;
  }

  public static Collection<? extends GrantedAuthority> getAuthorities(EUserRole userRole) {
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(userRole.toString()));
    return authorities;
  }

  public LoginResponseDto login(LoginDto loginDto) {
    Auth auth =
        authRepository
            .findByUsername(loginDto.getUsername())
            .orElseThrow(
                () -> new BadCredentialsException(ExceptionMessages.INVALID_CREDENTIALS_MESSAGE));
    validateLogin(auth, loginDto);
    return new LoginResponseDto(jwtService.createToken(auth).getValue());
  }

  @Transactional
  public void register(RegistrationDto registrationDto) {
    if (authRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
      log.error(
          "User with the provided username '{}' already exists!", registrationDto.getUsername());
      throw new UsernameAlreadyExistsException(ExceptionMessages.USERNAME_ALREADY_EXISTS);
    }

    if (!isRoleAllowed(registrationDto.getUserRole())) {
      throw new ForbiddenRoleException(ExceptionMessages.INVALID_REQUEST);
    }

    Person person = this.personService.save(new Person(registrationDto));

    Auth auth =
        this.authRepository.save(
            Auth.builder()
                .username(registrationDto.getUsername())
                .active(false)
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

  public void confirmRegistration(ConfirmRegistrationDto confirmRegistrationDto) {
    TemporaryToken temporaryToken =
        temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            confirmRegistrationDto.getToken());

    Auth auth = temporaryToken.getAuth();
    auth.setActive(true);
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
  public void changePassword(Long userId, ChangePasswordDto changePasswordDto) {
    Auth auth = findByIdOrThrowException(userId);
    if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), auth.getPassword())) {
      throw new BadCredentialsException(ExceptionMessages.INVALID_CREDENTIALS_MESSAGE);
    }
    auth.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
    authRepository.save(auth);

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

  public Auth findByIdOrThrowException(Long id) {
    return authRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.USERNAME_DOES_NOT_EXISTS));
  }

  @Transactional
  public void createChangeEmailRequest(final Long authId, final EmailDto emaildto) {
    checkIfAllowed(authId);
    final Auth auth = findByIdOrThrowException(authId);
    final TemporaryToken token =
        temporaryTokenService.createToken(auth, ETemporaryTokenType.EMAIL_CHANGE_TOKEN);

    final EmailChangeRequest emailChangeRequest =
        EmailChangeRequest.builder()
            .newEmail(emaildto.getEmail())
            .person(auth.getPerson())
            .temporaryToken(token)
            .build();
    emailChangeRequestService.save(emailChangeRequest);
    log.info("Token for change email {}", token.getValue());
    emailService.sendMailToUser(
        new MailContentHolder(
            emailChangeRequest.getNewEmail(),
            EEmailType.CHANGE_EMAIL,
            Collections.singletonMap(EmailServiceImpl.EMAIL_CHANGE_TOKEN, token.getValue())));
  }

  private Boolean isRoleAllowed(EUserRole role) {
    return ALLOWED_ROLES.contains(role);
  }

  private void validateLogin(Auth auth, LoginDto loginDto) {
    if (!auth.getActive()) {
      log.error("User with auth id '{}' is not active ", auth.getId());
      throw new BadCredentialsException(ExceptionMessages.INVALID_CREDENTIALS_MESSAGE);
    }
    if (!passwordEncoder.matches(loginDto.getPassword(), auth.getPassword())) {
      log.error("User with auth id '{}' provided passwords which do not match ", auth.getId());
      throw new BadCredentialsException(ExceptionMessages.INVALID_CREDENTIALS_MESSAGE);
    }
  }

  private void checkIfAllowed(final Long authIdFromRequestPath) {
    if (!authIdFromRequestPath.equals(AuthenticationUtil.getAuthentication().getAuth().getId())) {
      throw new ForbiddenOperationException(ExceptionMessages.FORBIDDEN_OPERATION_EXCEPTION);
    }
  }
}
