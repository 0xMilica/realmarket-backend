package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.model.Auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public interface AuthService {
  void registerEntrepreneur(EntrepreneurRegistrationDto entrepreneurRegistrationDto);

  void registerInvestor(RegistrationDto registrationDto);

  RegistrationTokenInfoDto validateToken(String tokenValue);

  void initializeChangePassword(Long userId, ChangePasswordDto changePasswordDto);

  void finalizeChangePassword(Long authId, TwoFADto twoFACodeDto);

  TokenDto verifyPasswordAndReturnToken(Long authId, PasswordDto passwordDto);

  AuthResponseDto login(LoginDto loginDto, HttpServletRequest request);

  Auth findByUsernameOrThrowException(String username);

  Auth findByUserIdrThrowException(Long userId);

  Optional<Auth> findById(Long id);

  Auth findByIdOrThrowException(Long id);

  List<Auth> findAllInvestors();

  void confirmRegistration(ConfirmRegistrationDto confirmRegistrationDto);

  void recoverUsername(EmailDto emailDto);

  void initializeResetPassword(UsernameDto usernameDto);

  void finalizeResetPassword(ResetPasswordDto resetPasswordDto);

  void verifyEmailChangeRequest(final Long authId, final TwoFADto twoFACodeDto);

  void finalizeEmailChange(final ConfirmEmailChangeDto confirmEmailChangeDto);

  void logout(HttpServletRequest request, HttpServletResponse response);

  void updateSecretById(Long id, String secret);

  void initializeEmailChange(final Long authId, final EmailDto emailDto);

  void finalize2faInitialization(Auth auth);

  void checkLoginCredentials(Auth auth, String password);
}
