package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.TwoFADto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.AuthorizedAction;
import io.realmarket.propeler.model.OTPWildcard;
import io.realmarket.propeler.repository.OTPWildcardRepository;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.AuthorizedActionService;
import io.realmarket.propeler.service.OTPService;
import io.realmarket.propeler.service.util.RandomStringBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;

import static io.realmarket.propeler.model.enums.EAuthorizationActionType.NEW_TOTP_SECRET;

@Service
@Slf4j
public class OTPServiceImpl implements OTPService {

  private static final Integer SIZE_OF_OTP_CODE = 6;
  private AuthService authService; // needed for change secret
  private AuthorizedActionService authorizedActionService;
  private OTPWildcardRepository otpWildcardRepository;
  private PasswordEncoder passwordEncoder;

  @Value("${app.otp.secret.size}")
  private Integer otpSecretSize;

  @Value("${app.otp.wildcard.size}")
  private Integer otpWildcardSize;

  @Value("${app.otp.wildcard.batch_size}")
  private Integer otpWildcardBatchSize;

  @Autowired
  OTPServiceImpl(
      @Lazy AuthService authService,
      AuthorizedActionService authorizedActionService,
      OTPWildcardRepository otpWildcardRepository,
      PasswordEncoder passwordEncoder) {
    this.authService = authService;
    this.otpWildcardRepository = otpWildcardRepository;
    this.authorizedActionService = authorizedActionService;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public String generateTOTPSecret(Auth auth) {
    String secret = RandomStringBuilder.generateBase32String(otpSecretSize);
    String encryptedSecret = encryptSecret(secret);

    authorizedActionService.storeAuthorizationAction(
        auth.getId(), NEW_TOTP_SECRET, encryptedSecret, 3600 * 1000L);
    return secret;
  }

  @Transactional
  public List<String> generateRecoveryCodes(Auth auth) {
    LinkedList<String> recoveryCodes = new LinkedList<>();
    otpWildcardRepository.deleteAllByAuth(auth);
    for (int i = 0; i < otpWildcardBatchSize; i++) {
      String recoveryCode = RandomStringBuilder.generateBase32String(otpWildcardSize);
      recoveryCodes.add(recoveryCode);
      otpWildcardRepository.save(
          OTPWildcard.builder().auth(auth).wildcard(passwordEncoder.encode(recoveryCode)).build());
    }
    return recoveryCodes;
  }

  @Transactional
  public Boolean validate(Auth auth, TwoFADto code) {
    if (StringUtils.hasText(code.getCode())) {
      return validateCode(auth.getTotpSecret(), code.getCode());
    } else if (StringUtils.hasText(code.getWildcard())) {
      return validateRecoveryCode(auth, code.getWildcard());
    }
    return false;
  }

  @Transactional
  public Boolean validateTOTPSecretChange(Auth auth, String totpCode) {
    AuthorizedAction authorizedAction =
        authorizedActionService.findAuthorizedActionOrThrowException(auth, NEW_TOTP_SECRET);
    String newSecret = decryptSecret(authorizedAction.getData());
    if (!validateCode(newSecret, totpCode)) {
      return false;
    }
    // save new secret
    authService.updateSecretById(auth.getId(), newSecret);

    authorizedActionService.deleteByAuthAndType(auth, NEW_TOTP_SECRET);
    return true;
  }

  private boolean validateCode(String encriptedSecret, String code) {
    Totp totp = new Totp(decryptSecret(encriptedSecret));
    return totp.verify(code);
  }

  private boolean validateRecoveryCode(Auth auth, String code) {
    return validateRecoveryCode(auth.getId(), code);
  }

  private boolean validateRecoveryCode(Long authId, String code) {
    List<OTPWildcard> otpWildcards = otpWildcardRepository.findAllByAuth(new Auth(authId));
    for (OTPWildcard otpWildcard : otpWildcards) {
      if (passwordEncoder.matches(code, otpWildcard.getWildcard())) {
        otpWildcardRepository.deleteById(otpWildcard.getId());
        return true;
      }
    }
    return false;
  }

  // Decript/Encript secret Take salt from property and use it as key for encription
  private String decryptSecret(String encriptedSecret) {
    return encriptedSecret;
  }

  private String encryptSecret(String secret) {
    return secret;
  }
}
