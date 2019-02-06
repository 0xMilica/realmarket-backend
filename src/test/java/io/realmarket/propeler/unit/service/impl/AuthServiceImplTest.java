package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.model.enums.ETemporaryTokenType;
import io.realmarket.propeler.repository.AuthRepository;
import io.realmarket.propeler.service.EmailService;
import io.realmarket.propeler.service.PersonService;
import io.realmarket.propeler.service.TemporaryTokenService;
import io.realmarket.propeler.service.exception.ForbiddenRoleException;
import io.realmarket.propeler.service.exception.UsernameAlreadyExistsException;
import io.realmarket.propeler.service.impl.AuthServiceImpl;
import io.realmarket.propeler.service.impl.JWTServiceImpl;
import io.realmarket.propeler.service.util.dto.LoginResponseDto;
import io.realmarket.propeler.unit.util.AuthUtils;
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
import java.util.Optional;

import static io.realmarket.propeler.unit.util.AuthUtils.*;
import static io.realmarket.propeler.unit.util.JWTUtils.TEST_JWT;
import static io.realmarket.propeler.unit.util.PersonUtils.TEST_PERSON;
import static io.realmarket.propeler.unit.util.TemporaryTokenUtils.TEST_TEMPORARY_TOKEN;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AuthServiceImpl.class)
public class AuthServiceImplTest {
  @Mock private PasswordEncoder passwordEncoder;

  @Mock private EmailService emailService;

  @Mock private AuthRepository authRepository;

  @Mock private PersonService personService;

  @Mock private TemporaryTokenService temporaryTokenService;
  @Mock JWTServiceImpl jwtService;

  @InjectMocks private AuthServiceImpl authServiceImpl;

  @Test
  public void Register_Should_RegisterUser() {
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
    when(personService.save(TEST_PERSON)).thenReturn(TEST_PERSON);
    when(authRepository.save(any(Auth.class))).thenReturn(TEST_AUTH);
    when(temporaryTokenService.createToken(TEST_AUTH, ETemporaryTokenType.REGISTRATION_TOKEN))
        .thenReturn(TEST_TEMPORARY_TOKEN);
    when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(TEST_PASSWORD);
    doNothing().when(emailService).sendMailToUser(TEST_EMAIL_DTO);

    authServiceImpl.register(AuthUtils.TEST_REGISTRATION_DTO);

    verify(authRepository, Mockito.times(1)).findByUsername(TEST_USERNAME);
    verify(personService, Mockito.times(1)).save(TEST_PERSON);
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
  public void ChangePassword_Should_Throw_WrongPassword_OnWrongOldPassword() {
    when(passwordEncoder.matches(TEST_PASSWORD, TEST_PASSWORD)).thenReturn(false);
    when(authRepository.findById(TEST_AUTH_ID)).thenReturn(Optional.ofNullable(TEST_AUTH));

    authServiceImpl.changePassword(TEST_AUTH_ID, TEST_CHANGE_PASSWORD_DTO);
  }

  @Test
  public void ChangePassword_Should_SaveNewPassword() {
    when(passwordEncoder.matches(TEST_PASSWORD, TEST_PASSWORD)).thenReturn(true);
    when(passwordEncoder.encode(TEST_PASSWORD_NEW)).thenReturn(TEST_PASSWORD);
    when(authRepository.findById(TEST_AUTH_ID)).thenReturn(Optional.ofNullable(TEST_AUTH));
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(TEST_USER_AUTH);
    SecurityContextHolder.setContext(securityContext);

    authServiceImpl.changePassword(TEST_AUTH_ID, TEST_CHANGE_PASSWORD_DTO);

    verify(authRepository, times(1)).save(TEST_AUTH);
    // verify(tokenService,times(1)).deleteJWTsForUserExceptActiveOne(eq(TEST_AUTH_ID),anyString());
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
    when(temporaryTokenService.findByValueAndNotExpiredOrThrowException(
            TEST_REGISTRATION_TOKEN_VALUE))
        .thenReturn(TEST_TEMPORARY_TOKEN);

    final TemporaryToken mock = PowerMockito.spy(TEST_TEMPORARY_TOKEN);
    PowerMockito.when(mock, "getAuth").thenReturn(TEST_AUTH);
    when(authRepository.save(TEST_AUTH)).thenReturn(TEST_AUTH);
    doNothing().when(temporaryTokenService).deleteToken(TEST_TEMPORARY_TOKEN);

    authServiceImpl.confirmRegistration(AuthUtils.TEST_CONFIRM_REGISTRATION_DTO);

    verify(temporaryTokenService, Mockito.times(1))
        .findByValueAndNotExpiredOrThrowException(TEST_REGISTRATION_TOKEN_VALUE);
    verify(authRepository, Mockito.times(1)).save(any(Auth.class));
    assertEquals(TEST_AUTH.getActive(), true);
  }

  @Test
  public void Login_Should_Return_Valid_JWT_Token() {

    AuthServiceImpl authSpy = PowerMockito.spy(authServiceImpl);
    Auth auth = TEST_AUTH;
    auth.setActive(true);
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(auth));
    when(passwordEncoder.matches(TEST_LOGIN_DTO.getPassword(), auth.getPassword()))
        .thenReturn(true);

    when(jwtService.createToken(auth)).thenReturn(TEST_JWT);

    LoginResponseDto login = authSpy.login(TEST_LOGIN_DTO);

    assertEquals(TEST_JWT.getValue(), login.getJwt());
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
    auth.setActive(true);
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(auth));

    authSpy.login(TEST_LOGIN_DTO);
  }
}
