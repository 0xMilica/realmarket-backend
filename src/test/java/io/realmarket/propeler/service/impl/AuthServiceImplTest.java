package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.AuthResponseDto;
import io.realmarket.propeler.api.dto.EmailDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.AuthState;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.model.enums.AuthStateName;
import io.realmarket.propeler.model.enums.AuthorizedActionTypeName;
import io.realmarket.propeler.model.enums.TemporaryTokenTypeName;
import io.realmarket.propeler.repository.AuthRepository;
import io.realmarket.propeler.repository.AuthStateRepository;
import io.realmarket.propeler.repository.CountryRepository;
import io.realmarket.propeler.repository.UserRoleRepository;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.blockchain.BlockchainCommunicationService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.exception.UsernameAlreadyExistsException;
import io.realmarket.propeler.service.util.LoginIPAttemptsService;
import io.realmarket.propeler.service.util.LoginUsernameAttemptsService;
import io.realmarket.propeler.service.util.MailContentHolder;
import io.realmarket.propeler.util.AuthUtils;
import io.realmarket.propeler.util.OTPUtils;
import io.realmarket.propeler.util.RegistrationTokenUtils;
import io.realmarket.propeler.util.TwoFactorAuthUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Optional;

import static io.realmarket.propeler.service.impl.AuthServiceImpl.EMAIL_CHANGE_ACTION_MILLISECONDS;
import static io.realmarket.propeler.service.impl.AuthServiceImpl.PASSWORD_CHANGE_ACTION_MILLISECONDS;
import static io.realmarket.propeler.util.AuthUtils.*;
import static io.realmarket.propeler.util.JWTUtils.TEST_JWT_VALUE;
import static io.realmarket.propeler.util.PersonUtils.*;
import static io.realmarket.propeler.util.TemporaryTokenUtils.TEST_TEMPORARY_TOKEN;
import static io.realmarket.propeler.util.TwoFactorAuthUtils.TEST_2FA_DTO;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AuthServiceImpl.class)
public class AuthServiceImplTest {
  @Mock JWTServiceImpl jwtService;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private EmailService emailService;
  @Mock private AuthRepository authRepository;
  @Mock private RememberMeCookieService rememberMeCookieService;
  @Mock private PersonService personService;
  @Mock private TemporaryTokenService temporaryTokenService;
  @Mock private RegistrationTokenService registrationTokenService;
  @Mock private AuthorizedActionService authorizedActionService;
  @Mock private LoginIPAttemptsService loginIPAttemptsService;
  @Mock private LoginUsernameAttemptsService loginUsernameAttemptsService;
  @Mock private UserRoleRepository userRoleRepository;
  @Mock private AuthStateRepository authStateRepository;
  @Mock private CountryRepository countryRepository;
  @Mock private BlockchainCommunicationService blockchainCommunicationService;

  @InjectMocks private AuthServiceImpl authServiceImpl;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContext();
  }

  @Test
  public void RegisterEntrepreneur_Should_RegisterEntrepreneur() {
    when(registrationTokenService.findByValueAndNotExpiredOrThrowException(
            RegistrationTokenUtils.TEST_VALUE))
        .thenReturn(RegistrationTokenUtils.TEST_REGISTRATION_TOKEN);
    when(userRoleRepository.findByName(any())).thenReturn(Optional.of(AuthUtils.TEST_USER_ROLE));
    when(authStateRepository.findByName(any())).thenReturn(Optional.of(TEST_AUTH_STATE));
    when(countryRepository.findByCode(TEST_COUNTRY_CODE))
        .thenReturn(Optional.of(AuthUtils.TEST_COUNTRY));
    when(personService.save(TEST_REGISTRATION_PERSON)).thenReturn(TEST_REGISTRATION_PERSON);
    when(authRepository.save(any(Auth.class))).thenReturn(TEST_AUTH);
    when(temporaryTokenService.createToken(TEST_AUTH, TemporaryTokenTypeName.REGISTRATION_TOKEN))
        .thenReturn(TEST_TEMPORARY_TOKEN);
    doNothing().when(emailService).sendMailToUser(any(MailContentHolder.class));

    authServiceImpl.registerEntrepreneur(TEST_REGISTRATION_ENTREPRENEUR_DTO);

    verify(authRepository, Mockito.times(1)).save(any(Auth.class));
  }

  @Test
  public void RegisterInvestor_Should_RegisterInvestor() {
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
    when(personService.save(TEST_REGISTRATION_PERSON)).thenReturn(TEST_REGISTRATION_PERSON);
    when(authRepository.save(any(Auth.class))).thenReturn(TEST_AUTH);
    when(temporaryTokenService.createToken(TEST_AUTH, TemporaryTokenTypeName.REGISTRATION_TOKEN))
        .thenReturn(TEST_TEMPORARY_TOKEN);
    when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(TEST_PASSWORD);
    when(userRoleRepository.findByName(any())).thenReturn(Optional.of(AuthUtils.TEST_USER_ROLE));
    when(authStateRepository.findByName(any())).thenReturn(Optional.of(TEST_AUTH_STATE));
    when(countryRepository.findByCode(TEST_COUNTRY_CODE))
        .thenReturn(Optional.of(AuthUtils.TEST_COUNTRY));
    when(countryRepository.findByCode(TEST_COUNTRY_CODE2))
        .thenReturn(Optional.of(AuthUtils.TEST_COUNTRY2));
    doNothing().when(emailService).sendMailToUser(any(MailContentHolder.class));

    authServiceImpl.registerInvestor(AuthUtils.TEST_REGISTRATION_DTO);

    verify(authRepository, Mockito.times(1)).findByUsername(TEST_USERNAME);
    verify(personService, Mockito.times(1)).save(TEST_REGISTRATION_PERSON);
    verify(authRepository, Mockito.times(1)).save(any(Auth.class));
    verify(passwordEncoder, Mockito.times(1)).encode(TEST_PASSWORD);
  }

  @Test
  public void ValidateToken_Should_ReturnTokenInformation() {
    when(registrationTokenService.findByValueAndNotExpiredOrThrowException(
            RegistrationTokenUtils.TEST_VALUE))
        .thenReturn(RegistrationTokenUtils.TEST_REGISTRATION_TOKEN);

    authServiceImpl.validateToken(RegistrationTokenUtils.TEST_VALUE);

    verify(registrationTokenService, Mockito.times(1))
        .findByValueAndNotExpiredOrThrowException(RegistrationTokenUtils.TEST_VALUE);
  }

  @Test
  public void VerifyPasswordAndReturnToken_Should_CreateToken() {
    when(temporaryTokenService.createToken(any(), any())).thenReturn(TEST_TEMPORARY_TOKEN);
    when(authRepository.findById(TEST_AUTH_ID)).thenReturn(Optional.of(TEST_AUTH));
    when(passwordEncoder.matches(TEST_AUTH.getPassword(), TEST_PASSWORD_DTO.getPassword()))
        .thenReturn(true);

    authServiceImpl.verifyPasswordAndReturnToken(TEST_AUTH_ID, TEST_PASSWORD_DTO);

    verify(temporaryTokenService, times(1)).createToken(any(), any());
  }

  @Test(expected = UsernameAlreadyExistsException.class)
  public void RegisterInvestor_Should_Throw_UsernameAlreadyExistsException_WhenUsernameExists() {
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(TEST_AUTH));

    authServiceImpl.registerInvestor(AuthUtils.TEST_REGISTRATION_DTO);
  }

  @Test(expected = BadCredentialsException.class)
  public void InitializeChangePassword_Should_Throw_WrongPassword_OnWrongOldPassword() {
    when(passwordEncoder.matches(TEST_PASSWORD, TEST_PASSWORD)).thenReturn(false);
    when(authRepository.findById(TEST_AUTH_ID)).thenReturn(Optional.ofNullable(TEST_AUTH));

    authServiceImpl.initializeChangePassword(TEST_AUTH_ID, TEST_CHANGE_PASSWORD_DTO);
  }

  @Test
  public void InitializeChangePassword_Should_StoreAuthorizedAction() {
    when(passwordEncoder.matches(TEST_PASSWORD, TEST_PASSWORD)).thenReturn(true);
    when(passwordEncoder.encode(TEST_PASSWORD_NEW)).thenReturn(TEST_PASSWORD);
    when(authRepository.findById(TEST_AUTH_ID)).thenReturn(Optional.ofNullable(TEST_AUTH));
    doNothing()
        .when(authorizedActionService)
        .storeAuthorizationAction(
            TEST_AUTH_ID,
            AuthorizedActionTypeName.NEW_PASSWORD,
            TEST_PASSWORD,
            PASSWORD_CHANGE_ACTION_MILLISECONDS);

    authServiceImpl.initializeChangePassword(TEST_AUTH_ID, TEST_CHANGE_PASSWORD_DTO);

    verify(authorizedActionService, Mockito.times(1))
        .storeAuthorizationAction(
            TEST_AUTH_ID,
            AuthorizedActionTypeName.NEW_PASSWORD,
            TEST_PASSWORD,
            PASSWORD_CHANGE_ACTION_MILLISECONDS);
  }

  @Test
  public void FinalizeChangePassword_Should_UpdateUserPassword() {
    when(authRepository.findById(TEST_AUTH_ID)).thenReturn(Optional.ofNullable(TEST_AUTH));
    when(authorizedActionService.validateAuthorizationAction(
            TEST_AUTH, AuthorizedActionTypeName.NEW_PASSWORD, TEST_2FA_DTO))
        .thenReturn(Optional.of(TEST_PASSWORD));
    when(authRepository.save(TEST_AUTH)).thenReturn(TEST_AUTH);
    doNothing().when(jwtService).deleteAllByAuthAndValueNot(TEST_AUTH, TEST_JWT_VALUE);
    doNothing()
        .when(authorizedActionService)
        .deleteByAuthAndType(TEST_AUTH, AuthorizedActionTypeName.NEW_PASSWORD);

    authServiceImpl.finalizeChangePassword(TEST_AUTH_ID, TEST_2FA_DTO);

    verify(authRepository, times(1)).save(TEST_AUTH);
    verify(authorizedActionService, times(1))
        .deleteByAuthAndType(TEST_AUTH, AuthorizedActionTypeName.NEW_PASSWORD);
    verify(jwtService, times(1)).deleteAllByAuthAndValueNot(eq(TEST_AUTH), anyString());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void FinalizeChangePassword_Should_Throw_ForbiddenOperationException() {
    when(authRepository.findById(TEST_AUTH_ID)).thenReturn(Optional.ofNullable(TEST_AUTH));
    when(authorizedActionService.validateAuthorizationAction(
            TEST_AUTH, AuthorizedActionTypeName.NEW_PASSWORD, TEST_2FA_DTO))
        .thenReturn(Optional.empty());

    authServiceImpl.finalizeChangePassword(TEST_AUTH_ID, TEST_2FA_DTO);
  }

  @Test
  public void FindByIdOrThrowException_Should_ReturnAuth_IfUserExists() {
    when(authRepository.findById(TEST_AUTH_ID)).thenReturn(Optional.of(TEST_AUTH));

    Auth retVal = authServiceImpl.findByIdOrThrowException(TEST_AUTH_ID);

    assertEquals(TEST_AUTH, retVal);
  }

  @Test(expected = EntityNotFoundException.class)
  public void FindByIdOrThrowException_Should_ThrowException_IfUserExists() {
    when(authRepository.findById(TEST_AUTH_ID)).thenReturn(Optional.empty());

    authServiceImpl.findByIdOrThrowException(TEST_AUTH_ID);
  }

  @Test
  public void FindByUsernameOrThrowException_Should_ReturnAuth_IfUserExists() {
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(TEST_AUTH));

    Auth retVal = authServiceImpl.findByUsernameOrThrowException(TEST_USERNAME);

    assertEquals(TEST_AUTH, retVal);
  }

  @Test(expected = EntityNotFoundException.class)
  public void FindByUsernameOrThrowException_Should_ThrowException_IfUserNotExists() {
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

    authServiceImpl.findByUsernameOrThrowException(TEST_USERNAME);
  }

  @Test
  public void ConfirmRegistration_Should_ActivateUser() throws Exception {
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(TEST_TEMPORARY_TOKEN_VALUE))
        .thenReturn(TEST_TEMPORARY_TOKEN);

    final TemporaryToken mock = PowerMockito.spy(TEST_TEMPORARY_TOKEN);
    PowerMockito.when(mock, "getAuth").thenReturn(TEST_AUTH);
    when(authRepository.save(TEST_AUTH)).thenReturn(TEST_AUTH);
    when(authStateRepository.findByName(any())).thenReturn(Optional.of(TEST_AUTH_STATE));
    doNothing().when(temporaryTokenService).deleteToken(TEST_TEMPORARY_TOKEN);

    authServiceImpl.confirmRegistration(AuthUtils.TEST_CONFIRM_REGISTRATION_DTO);

    verify(temporaryTokenService, Mockito.times(1))
        .findByValueAndNotExpiredOrThrowException(TEST_TEMPORARY_TOKEN_VALUE);
    verify(authRepository, Mockito.times(1)).save(any(Auth.class));
    assertEquals(AuthStateName.ACTIVE, TEST_AUTH.getState().getName());
  }

  @Test
  public void RecoverUsername_Should_SendEmailWithUsernameList() {
    when(personService.findByEmail(TEST_EMAIL)).thenReturn(TEST_PERSON_LIST);
    doNothing().when(emailService).sendMailToUser(any(MailContentHolder.class));

    authServiceImpl.recoverUsername(new EmailDto(TEST_EMAIL));

    verify(personService, Mockito.times(1)).findByEmail(TEST_EMAIL);
    verify(emailService, times(1)).sendMailToUser(any(MailContentHolder.class));
  }

  @Test(expected = EntityNotFoundException.class)
  public void RecoverUsername_Should_Throw_EntityNotFoundException() {
    when(personService.findByEmail(TEST_EMAIL)).thenReturn(Collections.emptyList());

    authServiceImpl.recoverUsername(new EmailDto(TEST_EMAIL));
  }

  @Test
  public void InitializeResetPassword_Should_CreateResetPasswordRequest() {
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(TEST_AUTH));
    when(temporaryTokenService.createToken(TEST_AUTH, TemporaryTokenTypeName.RESET_PASSWORD_TOKEN))
        .thenReturn(TEST_TEMPORARY_TOKEN);
    doNothing().when(emailService).sendMailToUser(any(MailContentHolder.class));

    authServiceImpl.initializeResetPassword(TEST_USERNAME_DTO);

    verify(authRepository, Mockito.times(1)).findByUsername(TEST_USERNAME);
    verify(temporaryTokenService, Mockito.times(1))
        .createToken(TEST_AUTH, TemporaryTokenTypeName.RESET_PASSWORD_TOKEN);
  }

  @Test(expected = EntityNotFoundException.class)
  public void InitializeResetPassword_Should_Throw_EntityNotFoundException() {
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

    authServiceImpl.initializeResetPassword(TEST_USERNAME_DTO);
  }

  @Test
  public void FinalizeResetPassword_Should_ChangeUserPassword() throws Exception {
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(TEST_TEMPORARY_TOKEN_VALUE))
        .thenReturn(TEST_TEMPORARY_TOKEN);
    when(passwordEncoder.encode(TEST_PASSWORD_NEW)).thenReturn(TEST_PASSWORD);

    final TemporaryToken mock = PowerMockito.spy(TEST_TEMPORARY_TOKEN);
    PowerMockito.when(mock, "getAuth").thenReturn(TEST_AUTH);
    when(authRepository.save(TEST_AUTH)).thenReturn(TEST_AUTH);

    authServiceImpl.finalizeResetPassword(TEST_RESET_PASSWORD_DTO);

    verify(temporaryTokenService, Mockito.times(1))
        .findByValueAndNotExpiredOrThrowException(TEST_TEMPORARY_TOKEN_VALUE);
    verify(authRepository, times(1)).save(TEST_AUTH);
  }

  @Test
  public void Login_Should_Return_Valid_JWT_Token() {
    AuthServiceImpl authSpy = PowerMockito.spy(authServiceImpl);
    Auth auth = TEST_AUTH.toBuilder().build();
    auth.setState(AuthState.builder().name(AuthStateName.ACTIVE).id(100L).build());
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(auth));
    when(passwordEncoder.matches(TEST_LOGIN_DTO.getPassword(), auth.getPassword()))
        .thenReturn(true);
    when(temporaryTokenService.createToken(any(), any())).thenReturn(TEST_TEMPORARY_TOKEN);

    when(jwtService.createToken(auth)).thenReturn(TEST_JWT_VALUE);

    AuthResponseDto login = authSpy.login(TEST_LOGIN_DTO, TEST_REQUEST);
    verify(loginIPAttemptsService, (times(1))).loginSucceeded(any());
    verify(loginUsernameAttemptsService, (times(1))).loginSucceeded(any());
    assertEquals(TEST_TEMPORARY_TOKEN.getValue(), login.getToken());
  }

  @Test(expected = BadCredentialsException.class)
  public void Login_Should_Throw_Exception_When_Not_Existing_Username() {
    // AuthServiceImpl authSpy = PowerMockito.spy(authServiceImpl);
    authServiceImpl.login(TEST_LOGIN_DTO, TEST_REQUEST);
    verify(loginIPAttemptsService, times(1)).loginFailed(any());
  }

  @Test(expected = BadCredentialsException.class)
  public void Login_Should_Throw_Exception_When_User_Not_Active() {

    AuthServiceImpl authSpy = PowerMockito.spy(authServiceImpl);
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(TEST_AUTH));

    authSpy.login(TEST_LOGIN_DTO, TEST_REQUEST);
  }

  @Test(expected = BadCredentialsException.class)
  public void Login_Should_Throw_Exception_When_Bad_Password() {

    AuthServiceImpl authSpy = PowerMockito.spy(authServiceImpl);

    Auth auth = TEST_AUTH;
    auth.setState(AuthState.builder().name(AuthStateName.ACTIVE).id(101L).build());
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(auth));

    authSpy.login(TEST_LOGIN_DTO, TEST_REQUEST);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void Login_Should_Return_ForbiddenOperationException_Exception_When_Account_Blocked() {
    AuthServiceImpl authSpy = PowerMockito.spy(authServiceImpl);
    Auth auth = TEST_AUTH.toBuilder().build();
    auth.setState(AuthState.builder().name(AuthStateName.ACTIVE).id(102L).build());
    auth.setBlocked(true);
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(auth));
    authSpy.login(TEST_LOGIN_DTO, TEST_REQUEST);
  }
  // TODO : login with remember me cookie returns 2fa status REMEMBER_ME

  @Test
  public void Logout_Should_Remove_JWT_Token() {
    authServiceImpl.logout(TEST_REQUEST, TEST_RESPONSE);
    verify(jwtService, times(1)).deleteByValue(TEST_USER_AUTH.getToken());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void InitializeEmailChange_Should_Throw_Exception_When_Not_Allowed() {
    final EmailDto emailDto = EmailDto.builder().email(TEST_EMAIL).build();
    authServiceImpl.initializeEmailChange(1000L, emailDto);
  }

  @Test
  public void InitializeEmailChange_Should_StoreAuthorizedAction() {

    Auth auth = TEST_AUTH.toBuilder().build();
    final EmailDto emailDto = EmailDto.builder().email(TEST_EMAIL).build();
    authServiceImpl.initializeEmailChange(TEST_AUTH_ID, emailDto);
    verify(authorizedActionService, times(1))
        .storeAuthorizationAction(
            auth.getId(),
            AuthorizedActionTypeName.NEW_EMAIL,
            emailDto.getEmail(),
            EMAIL_CHANGE_ACTION_MILLISECONDS);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void VerifyChangeEmailRequest_Should_Throw_Exception_When_Not_Allowed() {
    authServiceImpl.verifyEmailChangeRequest(1000L, TwoFactorAuthUtils.TEST_2FA_DTO);
  }

  @Test(expected = EntityNotFoundException.class)
  public void VerifyChangeEmailRequest_Should_Throw_Exception_When_Not_Existing_AuthId() {
    authServiceImpl.verifyEmailChangeRequest(TEST_AUTH.getId(), TwoFactorAuthUtils.TEST_2FA_DTO);
  }

  @Test
  public void VerifyChangeEmailRequest_Should_Create_Token_And_Send_Mail() {
    Auth auth = TEST_AUTH;
    when(authRepository.findById(auth.getId())).thenReturn(Optional.of(auth));
    when(temporaryTokenService.createToken(auth, TemporaryTokenTypeName.EMAIL_CHANGE_TOKEN))
        .thenReturn(TEST_TEMPORARY_TOKEN);
    when(authorizedActionService.validateAuthorizationAction(
            auth, AuthorizedActionTypeName.NEW_EMAIL, TwoFactorAuthUtils.TEST_2FA_DTO))
        .thenReturn(Optional.of(TEST_EMAIL));
    authServiceImpl.verifyEmailChangeRequest(TEST_AUTH.getId(), TwoFactorAuthUtils.TEST_2FA_DTO);

    verify(authRepository, times(1)).findById(auth.getId());
    verify(temporaryTokenService, times(1))
        .createToken(auth, TemporaryTokenTypeName.EMAIL_CHANGE_TOKEN);
    verify(emailService, times(1)).sendMailToUser(any());
  }

  @Test
  public void FinalizeEmailChange_Should_ChangeEmail() {
    TEST_PERSON.setEmail(TEST_NEW_EMAIL);
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            TEST_CONFIRM_EMAIL_CHANGE_DTO.getToken()))
        .thenReturn(TEST_TEMPORARY_TOKEN);
    when(personService.save(any(Person.class))).thenReturn(TEST_PERSON);
    when(authorizedActionService.findAuthorizedActionOrThrowException(
            TEST_TEMPORARY_TOKEN.getAuth(), AuthorizedActionTypeName.NEW_EMAIL))
        .thenReturn(OTPUtils.TEST_AUTH_ACTION_NEWEMAIL());
    authServiceImpl.finalizeEmailChange(TEST_CONFIRM_EMAIL_CHANGE_DTO);
    verify(temporaryTokenService, Mockito.times(1))
        .findByValueAndNotExpiredOrThrowException(TEST_CONFIRM_EMAIL_CHANGE_DTO.getToken());
    verify(personService, times(1)).save(any(Person.class));
    verify(temporaryTokenService, times(1)).deleteToken(TEST_TEMPORARY_TOKEN);
    verify(authorizedActionService, times(1))
        .deleteByAuthAndType(
            OTPUtils.TEST_AUTH_ACTION_NEWEMAIL().getAuth(),
            OTPUtils.TEST_AUTH_ACTION_NEWEMAIL().getType().getName());
  }

  @Test(expected = EntityNotFoundException.class)
  public void FinalizeEmailChange_Should_ThrowEntityNotFoundException_WhenNotValidToken() {
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            TEST_CONFIRM_EMAIL_CHANGE_DTO.getToken()))
        .thenThrow(EntityNotFoundException.class);
    authServiceImpl.finalizeEmailChange(TEST_CONFIRM_EMAIL_CHANGE_DTO);
  }

  @Test(expected = EntityNotFoundException.class)
  public void FinalizeEmailChange_Should_ThrowEntityNotFoundException_WhenNotCorrespondingEmail() {
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            TEST_CONFIRM_EMAIL_CHANGE_DTO.getToken()))
        .thenReturn(TEST_TEMPORARY_TOKEN);
    when(authorizedActionService.findAuthorizedActionOrThrowException(any(), any()))
        .thenThrow(EntityNotFoundException.class);
    authServiceImpl.finalizeEmailChange(TEST_CONFIRM_EMAIL_CHANGE_DTO);
  }
}
