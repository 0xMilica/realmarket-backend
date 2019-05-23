package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.model.TemporaryTokenType;
import io.realmarket.propeler.model.enums.ETemporaryTokenType;
import io.realmarket.propeler.repository.TemporaryTokenRepository;
import io.realmarket.propeler.repository.TemporaryTokenTypeRepository;
import io.realmarket.propeler.service.TemporaryTokenService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.InvalidTokenException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.RandomStringBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class TemporaryTokenServiceImpl implements TemporaryTokenService {
  private static final int TOKEN_LENGTH = 36;
  private final TemporaryTokenRepository temporaryTokenRepository;
  private final TemporaryTokenTypeRepository temporaryTokenTypeRepository;

  @Autowired
  public TemporaryTokenServiceImpl(
      TemporaryTokenRepository temporaryTokenRepository,
      TemporaryTokenTypeRepository temporaryTokenTypeRepository) {
    this.temporaryTokenRepository = temporaryTokenRepository;
    this.temporaryTokenTypeRepository = temporaryTokenTypeRepository;
  }

  private static Long getExpirationTime(ETemporaryTokenType tokenType) {
    switch (tokenType) {
      case SETUP_2FA_TOKEN:
      case EMAIL_CHANGE_TOKEN:
        return 1800000L;
      case EMAIL_TOKEN:
      case PASSWORD_VERIFIED_TOKEN:
        return 300000L; // 5min
      case LOGIN_TOKEN:
      case REGISTRATION_TOKEN:
      case RESET_PASSWORD_TOKEN:
        return 86400000L; // 24h
      default:
        throw new EntityNotFoundException(ExceptionMessages.INVALID_TOKEN_TYPE);
    }
  }

  @Transactional
  public boolean validateTokenAndDeleteIt(String value, ETemporaryTokenType type) {
    Optional<TemporaryToken> temporaryToken =
        temporaryTokenRepository.findByValueAndExpirationTimeGreaterThanEqual(value, Instant.now());
    if (!temporaryToken.isPresent() || temporaryToken.get().getTemporaryTokenType().equals(type)) {
      return false;
    }
    deleteToken(temporaryToken.get());
    return true;
  }

  @Transactional
  public TemporaryToken createToken(Auth auth, ETemporaryTokenType type) {

    deleteByTemporaryTokenTypeAndAuthId(auth.getId(), type);
    Optional<TemporaryTokenType> tokenType = this.temporaryTokenTypeRepository.findByName(type);
    if (!tokenType.isPresent()) {
      throw new BadRequestException(ExceptionMessages.INVALID_TOKEN_TYPE);
    }

    return temporaryTokenRepository.save(
        TemporaryToken.builder()
            .value(RandomStringBuilder.generateBase32String(TOKEN_LENGTH))
            .auth(auth)
            .temporaryTokenType(tokenType.get())
            .expirationTime(Instant.now().plusMillis(getExpirationTime(type)))
            .build());
  }

  private void deleteByTemporaryTokenTypeAndAuthId(Long authId, ETemporaryTokenType type) {
    temporaryTokenRepository.deleteByTemporaryTokenTypeNameAndAuthId(type, authId);
    temporaryTokenRepository.flush();
  }

  public void deleteToken(TemporaryToken temporaryToken) {
    temporaryTokenRepository.delete(temporaryToken);
  }

  public TemporaryToken findByValueAndNotExpiredOrThrowException(String value) {
    return temporaryTokenRepository
        .findByValueAndExpirationTimeGreaterThanEqual(value, Instant.now())
        .orElseThrow(() -> new InvalidTokenException(ExceptionMessages.INVALID_TOKEN_PROVIDED));
  }

  @Override
  public TemporaryToken findByValueAndTypeOrThrowException(String value, ETemporaryTokenType type) {
    return temporaryTokenRepository
        .findByValueAndTemporaryTokenTypeAndExpirationTimeGreaterThanEqual(
            value, type, Instant.now())
        .orElseThrow(() -> new InvalidTokenException(ExceptionMessages.INVALID_TOKEN_PROVIDED));
  }

  @Transactional
  @Scheduled(
      fixedRateString = "${app.cleanse.tokens.timeloop}",
      initialDelayString = "${app.cleanse.tokens.timeloop}")
  public void deleteExpiredTokens() {
    log.trace("Clean failed registrations");
    temporaryTokenRepository.deleteAllByExpirationTimeLessThan(Instant.now());
  }
}
