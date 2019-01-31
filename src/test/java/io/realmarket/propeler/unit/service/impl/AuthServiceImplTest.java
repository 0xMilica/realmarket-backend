package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.repository.AuthRepository;
import io.realmarket.propeler.service.EmailService;
import io.realmarket.propeler.service.PersonService;
import io.realmarket.propeler.service.exception.ForbiddenRoleException;
import io.realmarket.propeler.service.exception.InvalidTokenException;
import io.realmarket.propeler.service.exception.UsernameAlreadyExistsException;
import io.realmarket.propeler.service.impl.AuthServiceImpl;
import io.realmarket.propeler.unit.helpers.TokenValidatorTest;
import io.realmarket.propeler.unit.util.AuthUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static io.realmarket.propeler.unit.util.AuthUtils.*;
import static io.realmarket.propeler.unit.util.PersonUtils.TEST_PERSON;
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

  @InjectMocks private AuthServiceImpl authServiceImpl;

  @Test
  public void Register_Should_RegisterUser() {
    when(authRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
    when(personService.save(TEST_PERSON)).thenReturn(TEST_PERSON);
    when(authRepository.save(any(Auth.class))).thenReturn(TEST_AUTH);
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

  @Test
  public void cleanFailedRegistrations_Should_Call_() {
    authServiceImpl.cleanseFailedRegistrations();
    verify(authRepository, Mockito.times(1))
        .deleteByRegistrationTokenExpirationTimeLessThanAndActiveIsFalse(any());
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

  @Test(expected = InvalidTokenException.class)
  public void
      FindByRegistrationTokenOrThrowException_Should_ThrowInvalidTokenException_IfInvalidTokenProvided() {
    when(authRepository.findByRegistrationToken(TEST_AUTH_TOKEN)).thenReturn(Optional.empty());

    authServiceImpl.findByRegistrationTokenOrThrowException(TEST_AUTH_TOKEN);
  }

  @Test
  public void FindByRegistrationTokenOrThrowException_Should_ReturnAuth_IfTokenExists() {
    when(authRepository.findByRegistrationToken(TEST_AUTH_TOKEN))
        .thenReturn(Optional.of(TEST_AUTH));

    Auth retVal = authServiceImpl.findByRegistrationTokenOrThrowException(TEST_AUTH_TOKEN);

    assertEquals(TEST_AUTH, retVal);
  }

  @Test
  public void ConfirmRegistration_Should_ActivateUser() {
    Auth auth = TEST_AUTH;
    auth.setRegistrationTokenExpirationTime(TokenValidatorTest.TEST_DATE_IN_FUTURE);
    when(authRepository.findByRegistrationToken(TEST_AUTH_TOKEN)).thenReturn(Optional.of(auth));

    authServiceImpl.confirmRegistration(AuthUtils.TEST_CONFIRM_REGISTRATION_DTO);

    verify(authRepository, Mockito.times(1)).findByRegistrationToken(TEST_AUTH_TOKEN);
    verify(authRepository, Mockito.times(1)).save(any(Auth.class));
  }

  @Test(expected = InvalidTokenException.class)
  public void ConfirmRegistration_Should_ThrowExceptionInvalidTokenException_WhenTokenNotExists() {
    when(authRepository.findByRegistrationToken(TEST_AUTH_TOKEN)).thenReturn(Optional.empty());

    authServiceImpl.confirmRegistration(AuthUtils.TEST_CONFIRM_REGISTRATION_DTO);

    verify(authRepository, Mockito.times(1)).findByRegistrationToken(TEST_AUTH_TOKEN);
    verify(authRepository, Mockito.times(0)).save(any(Auth.class));
  }

  @Test(expected = InvalidTokenException.class)
  public void ConfirmRegistration_Should_ThrowInvalidTokenProvided_WhenTokenExpired() {
    Auth auth = TEST_AUTH;
    auth.setRegistrationTokenExpirationTime(TokenValidatorTest.TEST_DATE_IN_PAST);
    when(authRepository.findByRegistrationToken(TEST_AUTH_TOKEN)).thenReturn(Optional.of(auth));

    authServiceImpl.confirmRegistration(AuthUtils.TEST_CONFIRM_REGISTRATION_DTO);

    verify(authRepository, Mockito.times(1)).findByRegistrationToken(TEST_AUTH_TOKEN);
    verify(authRepository, Mockito.times(0)).save(any(Auth.class));
  }
}
