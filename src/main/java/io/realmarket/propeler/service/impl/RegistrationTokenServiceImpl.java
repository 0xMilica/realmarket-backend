package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.FundraisingProposal;
import io.realmarket.propeler.model.RegistrationToken;
import io.realmarket.propeler.repository.RegistrationTokenRepository;
import io.realmarket.propeler.service.RegistrationTokenService;
import io.realmarket.propeler.service.exception.InvalidTokenException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.RandomStringBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Slf4j
public class RegistrationTokenServiceImpl implements RegistrationTokenService {

  private static final int TOKEN_LENGTH = 36;
  private final RegistrationTokenRepository registrationTokenRepository;
  @Value("${registration-token.expiration-time}")
  private Long tokenExpirationTime;

  @Autowired
  public RegistrationTokenServiceImpl(RegistrationTokenRepository registrationTokenRepository) {
    this.registrationTokenRepository = registrationTokenRepository;
  }

  @Override
  public RegistrationToken createToken(FundraisingProposal fundraisingProposal) {
    return registrationTokenRepository.save(
        RegistrationToken.builder()
            .value(RandomStringBuilder.generateBase32String(TOKEN_LENGTH))
            .fundraisingProposal(fundraisingProposal)
            .expirationTime(Instant.now().plusMillis(tokenExpirationTime))
            .build());
  }

  @Override
  public RegistrationToken findByValueAndNotExpiredOrThrowException(String value) {
    return registrationTokenRepository
        .findByValueAndExpirationTimeGreaterThanEqual(value, Instant.now())
        .orElseThrow(() -> new InvalidTokenException(ExceptionMessages.INVALID_TOKEN_PROVIDED));
  }

  @Override
  public void deleteToken(RegistrationToken registrationToken) {
    registrationTokenRepository.delete(registrationToken);
  }

  @Transactional
  @Scheduled(
      fixedRateString = "${app.cleanse.tokens.timeloop}",
      initialDelayString = "${app.cleanse.tokens.timeloop}")
  public void deleteExpiredTokens() {
    log.trace("Clean failed registrations");
    registrationTokenRepository.deleteAllByExpirationTimeLessThan(Instant.now());
  }
}
