package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.model.enums.ETemporaryTokenType;
import io.realmarket.propeler.repository.TemporaryTokenRepository;
import io.realmarket.propeler.service.TemporaryTokenService;
import io.realmarket.propeler.service.exception.InvalidTokenException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.RandomStringBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

@Service
@Slf4j
public class TemporaryTokenServiceImpl implements TemporaryTokenService {
  private static final int TOKEN_LENGTH = 36;
  private final TemporaryTokenRepository temporaryTokenRepository;

  @Autowired
  public TemporaryTokenServiceImpl(TemporaryTokenRepository temporaryTokenRepository) {
    this.temporaryTokenRepository = temporaryTokenRepository;
  }

  private static Long getExpirationTime(ETemporaryTokenType tokenType) {
    switch (tokenType) {
      case SETUP_2FA:
        return 1800000L;
      case EMAIL_TOKEN:
        return 300000L;
      case LOGIN_TOKEN:
      case REGISTRATION_TOKEN:
      case RESET_PASSWORD_TOKEN:
        return 86400000L;
      default:
        throw new EntityNotFoundException(ExceptionMessages.INVALID_TOKEN_TYPE);
    }
  }

  public TemporaryToken createToken(Auth auth, ETemporaryTokenType type) {
    return temporaryTokenRepository.save(
        TemporaryToken.builder()
            .value(RandomStringBuilder.generateToken(TOKEN_LENGTH))
            .auth(auth)
            .temporaryTokenType(type)
            .expirationTime(new Date(System.currentTimeMillis() + getExpirationTime(type)))
            .build());
  }

  public void deleteToken(TemporaryToken temporaryToken) {
    temporaryTokenRepository.delete(temporaryToken);
  }

  public TemporaryToken findByValueAndNotExpiredOrThrowException(String value) {
    return temporaryTokenRepository
        .findByValueAndExpirationTimeGreaterThanEqual(value, new Date())
        .orElseThrow(() -> new InvalidTokenException(ExceptionMessages.INVALID_TOKEN_PROVIDED));
  }

  @Transactional
  @Scheduled(
      fixedRateString = "${app.cleanse.tokens.timeloop}",
      initialDelayString = "${app.cleanse.tokens.timeloop}")
  public void deleteExpiredTokens() {
    log.trace("Clean failed registrations");
    temporaryTokenRepository.deleteAllByExpirationTimeLessThan(new Date());
  }
}
