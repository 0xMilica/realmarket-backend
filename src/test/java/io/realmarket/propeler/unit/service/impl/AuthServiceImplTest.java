package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.api.dto.AuthResponseDto;
import io.realmarket.propeler.api.dto.EmailDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.model.enums.EAuthState;
import io.realmarket.propeler.model.enums.EAuthorizationActionType;
import io.realmarket.propeler.model.enums.ETemporaryTokenType;
import io.realmarket.propeler.repository.AuthRepository;
import io.realmarket.propeler.security.UserAuthentication;
import io.realmarket.propeler.service.AuthorizedActionService;
import io.realmarket.propeler.service.EmailService;
import io.realmarket.propeler.service.PersonService;
import io.realmarket.propeler.service.TemporaryTokenService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.exception.ForbiddenRoleException;
import io.realmarket.propeler.service.exception.UsernameAlreadyExistsException;
import io.realmarket.propeler.service.impl.AuthServiceImpl;
import io.realmarket.propeler.service.impl.JWTServiceImpl;
import io.realmarket.propeler.service.util.MailContentHolder;
import io.realmarket.propeler.unit.util.AuthUtils;
import io.realmarket.propeler.unit.util.OTPUtils;
import io.realmarket.propeler.unit.util.TwoFactorAuthUtils;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Optional;

import static io.realmarket.propeler.service.impl.AuthServiceImpl.EMAIL_CHANGE_ACTION_MILLISECONDS;
import static io.realmarket.propeler.service.impl.AuthServiceImpl.PASSWORD_CHANGE_ACTION_MILLISECONDS;
import static io.realmarket.propeler.unit.util.AuthUtils.*;
import static io.realmarket.propeler.unit.util.JWTUtils.TEST_JWT;
import static io.realmarket.propeler.unit.util.JWTUtils.TEST_JWT_VALUE;
import static io.realmarket.propeler.unit.util.PersonUtils.*;
import static io.realmarket.propeler.unit.util.TemporaryTokenUtils.TEST_TEMPORARY_TOKEN;
import static io.realmarket.propeler.unit.util.TwoFactorAuthUtils.TEST_2FA_DTO;
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
  @Mock private PersonService personService;
  @Mock private TemporaryTokenService temporaryTokenService;
  @Mock private AuthorizedActionService authorizedActionService;
  @InjectMocks private AuthServiceImpl authServiceImpl;

  @Before
  public void setUpAuthContext() {
    UserAuthentication auth = TEST_USER_AUTH;
    auth.getAuth().setId(TEST_AUTH_ID);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  public void Register_Should_RegisterUser() {
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
    when(personService.save(TEST_REGISTRATION_PERSON)).thenReturn(TEST_REGISTRATION_PERSON);
    when(authRepository.save(any(Auth.class))).thenReturn(TEST_AUTH);
    when(temporaryTokenService.createToken(TEST_AUTH, ETemporaryTokenType.REGISTRATION_TOKEN))
        .thenReturn(TEST_TEMPORARY_TOKEN);
    when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(TEST_PASSWORD);
    doNothing().when(emailService).sendMailToUser(any(MailContentHolder.class));

    authServiceImpl.register(AuthUtils.TEST_REGISTRATION_DTO);

    verify(authRepository, Mockito.times(1)).findByUsername(TEST_USERNAME);
    verify(personService, Mockito.times(1)).save(TEST_REGISTRATION_PERSON);
    verify(authRepository, Mockito.times(1)).save(any(Auth.class));
    verify(passwordEncoder, Mockito.times(1)).encode(TEST_PASSWORD);
  }

  @Test(expected = UsernameAlreadyExistsException.class)
  public void Register_Should_Throw_UsernameAlreadyExistsException_WhenUsernameExists() {
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(TEST_AUTH));

    authServiceImpl.register(AuthUtils.TEST_REGISTRATION_DTO);
  }

  @Test(expected = ForbiddenRoleException.class)
  public void Register_Should_Throw_BadRequestException_WhenRoleNotAllowed() {
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

    authServiceImpl.register(AuthUtils.TEST_REGISTRATION_DTO_ROLE_NOT_ALLOWED);
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
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(TEST_USER_AUTH);
    SecurityContextHolder.setContext(securityContext);
    doNothing()
        .when(authorizedActionService)
        .storeAuthorizationAction(
            TEST_AUTH_ID, EAuthorizationActionType.NEW_PASSWORD, TEST_PASSWORD, PASSWORD_CHANGE_ACTION_MILLISECONDS);

    authServiceImpl.initializeChangePassword(TEST_AUTH_ID, TEST_CHANGE_PASSWORD_DTO);

    verify(authorizedActionService, Mockito.times(1))
        .storeAuthorizationAction(
            TEST_AUTH_ID, EAuthorizationActionType.NEW_PASSWORD, TEST_PASSWORD, PASSWORD_CHANGE_ACTION_MILLISECONDS);
  }

  @Test
  public void FinalizeChangePassword_Should_UpdateUserPassword() {
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(TEST_USER_AUTH);
    SecurityContextHolder.setContext(securityContext);
    when(authRepository.findById(TEST_AUTH_ID)).thenReturn(Optional.ofNullable(TEST_AUTH));
    when(authorizedActionService.validateAuthorizationAction(
            TEST_AUTH, EAuthorizationActionType.NEW_PASSWORD, TEST_2FA_DTO))
        .thenReturn(Optional.of(TEST_PASSWORD));
    when(authRepository.save(TEST_AUTH)).thenReturn(TEST_AUTH);
    doNothing().when(jwtService).deleteAllByAuthAndValueNot(TEST_AUTH, TEST_JWT_VALUE);
    doNothing()
        .when(authorizedActionService)
        .deleteByAuthAndType(TEST_AUTH, EAuthorizationActionType.NEW_PASSWORD);

    authServiceImpl.finalizeChangePassword(TEST_AUTH_ID, TEST_2FA_DTO);

    verify(authRepository, times(1)).save(TEST_AUTH);
    verify(authorizedActionService, times(1))
        .deleteByAuthAndType(TEST_AUTH, EAuthorizationActionType.NEW_PASSWORD);
    verify(jwtService, times(1)).deleteAllByAuthAndValueNot(eq(TEST_AUTH), anyString());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void FinalizeChangePassword_Should_Throw_ForbiddenOperationException() {
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(TEST_USER_AUTH);
    SecurityContextHolder.setContext(securityContext);
    when(authRepository.findById(TEST_AUTH_ID)).thenReturn(Optional.ofNullable(TEST_AUTH));
    when(authorizedActionService.validateAuthorizationAction(
            TEST_AUTH, EAuthorizationActionType.NEW_PASSWORD, TEST_2FA_DTO))
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
    doNothing().when(temporaryTokenService).deleteToken(TEST_TEMPORARY_TOKEN);

    authServiceImpl.confirmRegistration(AuthUtils.TEST_CONFIRM_REGISTRATION_DTO);

    verify(temporaryTokenService, Mockito.times(1))
        .findByValueAndNotExpiredOrThrowException(TEST_TEMPORARY_TOKEN_VALUE);
    verify(authRepository, Mockito.times(1)).save(any(Auth.class));
    assertEquals(EAuthState.INITIALIZE_2FA, TEST_AUTH.getState());
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
    when(temporaryTokenService.createToken(TEST_AUTH, ETemporaryTokenType.RESET_PASSWORD_TOKEN))
        .thenReturn(TEST_TEMPORARY_TOKEN);
    doNothing().when(emailService).sendMailToUser(any(MailContentHolder.class));

    authServiceImpl.initializeResetPassword(TEST_USERNAME_DTO);

    verify(authRepository, Mockito.times(1)).findByUsername(TEST_USERNAME);
    verify(temporaryTokenService, Mockito.times(1))
        .createToken(TEST_AUTH, ETemporaryTokenType.RESET_PASSWORD_TOKEN);
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
    auth.setState(EAuthState.ACTIVE);
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(auth));
    when(passwordEncoder.matches(TEST_LOGIN_DTO.getPassword(), auth.getPassword()))
        .thenReturn(true);
    when(temporaryTokenService.createToken(any(), any())).thenReturn(TEST_TEMPORARY_TOKEN);

    when(jwtService.createToken(auth)).thenReturn(TEST_JWT);

    AuthResponseDto login = authSpy.login(TEST_LOGIN_DTO);

    assertEquals(TEST_TEMPORARY_TOKEN.getValue(), login.getToken());
  }

  @Test(expected = BadCredentialsException.class)
  public void Login_Should_Throw_Exception_When_Not_Existing_Username() {

    AuthServiceImpl authSpy = PowerMockito.spy(authServiceImpl);
    authSpy.login(TEST_LOGIN_DTO);
  }

  @Test(expected = BadCredentialsException.class)
  public void Login_Should_Throw_Exception_When_User_Not_Active() {

    AuthServiceImpl authSpy = PowerMockito.spy(authServiceImpl);
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(TEST_AUTH));

    authSpy.login(TEST_LOGIN_DTO);
  }

  @Test(expected = BadCredentialsException.class)
  public void Login_Should_Throw_Exception_When_Bad_Password() {

    AuthServiceImpl authSpy = PowerMockito.spy(authServiceImpl);

    Auth auth = TEST_AUTH;
    auth.setState(EAuthState.ACTIVE);
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(auth));

    authSpy.login(TEST_LOGIN_DTO);
  }

  @Test
  public void Logout_Should_Remove_JWT_Token() {
    authServiceImpl.logout();
    verify(jwtService, times(1)).deleteByValue(TEST_USER_AUTH.getToken());
  }

  @Test(expected = ForbiddenOperationException.class)
  public void InitializeEmailChange_Should_Throw_Exception_When_Not_Allowed() {

    Auth auth = TEST_AUTH;
    auth.setId(1000L);
    final EmailDto emailDto = EmailDto.builder().email(TEST_EMAIL).build();
    authServiceImpl.initializeEmailChange(TEST_AUTH_ID, emailDto);
  }

  @Test
  public void InitializeEmailChange_Should_StoreAuthorizedAction() {

    Auth auth = TEST_AUTH;
    final EmailDto emailDto = EmailDto.builder().email(TEST_EMAIL).build();
    authServiceImpl.initializeEmailChange(TEST_AUTH_ID, emailDto);
    verify(authorizedActionService, times(1))
        .storeAuthorizationAction(
            auth.getId(),
            EAuthorizationActionType.NEW_EMAIL,
            emailDto.getEmail(),
            EMAIL_CHANGE_ACTION_MILLISECONDS);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void VerifyChangeEmailRequest_Should_Throw_Exception_When_Not_Allowed() {

    Auth auth = TEST_AUTH;
    auth.setId(1000L);
    authServiceImpl.verifyEmailChangeRequest(TEST_AUTH_ID, TwoFactorAuthUtils.TEST_2FA_DTO);
  }

  @Test(expected = EntityNotFoundException.class)
  public void VerifyChangeEmailRequest_Should_Throw_Exception_When_Not_Existing_AuthId() {
    authServiceImpl.verifyEmailChangeRequest(TEST_AUTH.getId(), TwoFactorAuthUtils.TEST_2FA_DTO);
  }

  @Test
  public void VerifyChangeEmailRequest_Should_Create_Token_And_Send_Mail() {
    Auth auth = TEST_AUTH;
    when(authRepository.findById(auth.getId())).thenReturn(Optional.of(auth));
    when(temporaryTokenService.createToken(auth, ETemporaryTokenType.EMAIL_CHANGE_TOKEN))
        .thenReturn(TEST_TEMPORARY_TOKEN);
    when(authorizedActionService.validateAuthorizationAction(
            auth, EAuthorizationActionType.NEW_EMAIL, TwoFactorAuthUtils.TEST_2FA_DTO))
        .thenReturn(Optional.of(TEST_EMAIL));
    authServiceImpl.verifyEmailChangeRequest(TEST_AUTH.getId(), TwoFactorAuthUtils.TEST_2FA_DTO);

    verify(authRepository, times(1)).findById(auth.getId());
    verify(temporaryTokenService, times(1))
        .createToken(auth, ETemporaryTokenType.EMAIL_CHANGE_TOKEN);
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
            TEST_TEMPORARY_TOKEN.getAuth(), EAuthorizationActionType.NEW_EMAIL))
        .thenReturn(OTPUtils.TEST_AUTH_ACTION_NEWEMAIL());
    authServiceImpl.finalizeEmailChange(TEST_CONFIRM_EMAIL_CHANGE_DTO);
    verify(temporaryTokenService, Mockito.times(1))
        .findByValueAndNotExpiredOrThrowException(TEST_CONFIRM_EMAIL_CHANGE_DTO.getToken());
    verify(personService, times(1)).save(any(Person.class));
    verify(temporaryTokenService, times(1)).deleteToken(TEST_TEMPORARY_TOKEN);
    verify(authorizedActionService, times(1))
        .deleteByAuthAndType(
            OTPUtils.TEST_AUTH_ACTION_NEWEMAIL().getAuth(),
            OTPUtils.TEST_AUTH_ACTION_NEWEMAIL().getType());
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
