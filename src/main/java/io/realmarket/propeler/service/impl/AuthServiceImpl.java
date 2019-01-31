package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.ChangePasswordDto;
import io.realmarket.propeler.api.dto.ConfirmRegistrationDto;
import io.realmarket.propeler.api.dto.EmailDto;
import io.realmarket.propeler.api.dto.RegistrationDto;
import io.realmarket.propeler.api.dto.enums.EEmailType;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.model.enums.ETemporaryTokenType;
import io.realmarket.propeler.model.enums.EUserRole;
import io.realmarket.propeler.repository.AuthRepository;
import io.realmarket.propeler.security.UserAuthentication;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.EmailService;
import io.realmarket.propeler.service.PersonService;
import io.realmarket.propeler.service.TemporaryTokenService;
import io.realmarket.propeler.service.exception.ForbiddenRoleException;
import io.realmarket.propeler.service.exception.UsernameAlreadyExistsException;
import io.realmarket.propeler.service.exception.WrongPasswordException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

  private static final List<EUserRole> ALLOWED_ROLES =
      Arrays.asList(EUserRole.ROLE_ENTREPRENEUR, EUserRole.ROLE_INVESTOR);

  private final AuthRepository authRepository;

  private final PersonService personService;
  private final EmailService emailService;
  private final TemporaryTokenService temporaryTokenService;

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public AuthServiceImpl(
      PasswordEncoder passwordEncoder,
      PersonService personService,
      EmailService emailService,
      AuthRepository authRepository,
      TemporaryTokenService temporaryTokenService) {
    this.passwordEncoder = passwordEncoder;
    this.personService = personService;
    this.emailService = emailService;
    this.authRepository = authRepository;
    this.temporaryTokenService = temporaryTokenService;
  }

  public static Collection<? extends GrantedAuthority> getAuthorities(EUserRole userRole) {
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(userRole.toString()));
    return authorities;
  }

  @Transactional
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
                .person(person)
                .build());

    TemporaryToken temporaryToken =
        temporaryTokenService.createToken(auth, ETemporaryTokenType.REGISTRATION_TOKEN);

    emailService.sendMailToUser(populateEmailDto(registrationDto, temporaryToken));

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

  public static String getCurrentToken() {
    UserAuthentication authentication =
            (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
    if(authentication == null){
      return "";
    }
    return authentication.getToken();
  }

  @Transactional
  @Override
  public void changePassword(Long userId, ChangePasswordDto changePasswordDto) {
    Auth auth = findByIdOrThrowException(userId);
    if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), auth.getPassword())) {
      throw new WrongPasswordException();
    }
    auth.setPassword(passwordEncoder.encode((changePasswordDto.getNewPassword())));
    authRepository.save(auth);
    //tokenService.deleteJWTsForUserExceptActiveOne(userId, getCurrentToken());
  }

  @Override
  public Optional<Auth> findById(Long id) {
    return authRepository.findById(id);
  }

  public Auth findByUsernameOrThrowException(String username) {
    return authRepository
        .findByUsername(username)
        .orElseThrow(
            () -> new EntityNotFoundException("User with provided username does not exists."));
  }

  public Auth findByIdOrThrowException(Long userId) {
    return authRepository
        .findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User with provided id does not exists."));
  }

  private EmailDto populateEmailDto(
      RegistrationDto registrationDto, TemporaryToken temporaryToken) {
    EmailDto emailDto = new EmailDto();
    emailDto.setEmail(registrationDto.getEmail());
    emailDto.setType(EEmailType.REGISTER);
    Map<String, Object> parameterMap = new HashMap<>();
    parameterMap.put(EmailServiceImpl.USERNAME, registrationDto.getUsername());
    parameterMap.put(EmailServiceImpl.ACTIVATION_TOKEN, temporaryToken.getValue());

    emailDto.setContent(parameterMap);
    return emailDto;
  }

  private Boolean isRoleAllowed(EUserRole role) {
    return ALLOWED_ROLES.contains(role);
  }
}
