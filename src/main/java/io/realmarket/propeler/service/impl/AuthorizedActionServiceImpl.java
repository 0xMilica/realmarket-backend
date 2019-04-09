package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.TwoFADto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.AuthorizedAction;
import io.realmarket.propeler.model.enums.EAuthorizedActionType;
import io.realmarket.propeler.repository.AuthorizedActionRepository;
import io.realmarket.propeler.service.AuthorizedActionService;
import io.realmarket.propeler.service.OTPService;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;

import static io.realmarket.propeler.model.enums.EAuthorizedActionType.NEW_TOTP_SECRET;

@Service
@Slf4j
public class AuthorizedActionServiceImpl implements AuthorizedActionService {
  private final AuthorizedActionRepository authorizedActionRepository;
  private final OTPService otpService;

  @Autowired
  public AuthorizedActionServiceImpl(
      AuthorizedActionRepository authorizedActionRepository, @Lazy OTPService otpService) {
    this.authorizedActionRepository = authorizedActionRepository;
    this.otpService = otpService;
  }

  @Transactional
  public void storeAuthorizationAction(
          Long authId, EAuthorizedActionType type, String data, Long mmTimeout) {
    Auth auth = new Auth(authId);
    deleteByAuthAndType(auth, type);
    log.info("Store authorization action.");
    AuthorizedAction authorizedAction =
        AuthorizedAction.builder()
            .auth(auth)
            .type(type)
            .data(data)
            .expiration(Instant.now().plusMillis(mmTimeout))
            .build();
    authorizedActionRepository.save(authorizedAction);
  }

  public void deleteByAuthAndType(Auth authId, EAuthorizedActionType type) {
    authorizedActionRepository.deleteAllByAuthAndType(authId, type);
    authorizedActionRepository.flush();
  }

  public AuthorizedAction findAuthorizedActionOrThrowException(
      Auth auth, EAuthorizedActionType type) {
    Optional<AuthorizedAction> authorizedAction =
        authorizedActionRepository.findByAuthAndTypeAndExpirationIsAfter(auth, type, Instant.now());
    if (!authorizedAction.isPresent()) {
      throw new EntityNotFoundException(ExceptionMessages.AUTHORIZATION_ACTION_NOT_FOUND);
    }
    return authorizedAction.get();
  }

  @Transactional
  public Optional<String> validateAuthorizationAction(
          Auth auth, EAuthorizedActionType type, TwoFADto twoFADto) {
    if (type == NEW_TOTP_SECRET) {
      return Optional.empty();
    }
    AuthorizedAction authorizedAction = findAuthorizedActionOrThrowException(auth, type);

    if (otpService.validate(auth, twoFADto)) {
      return Optional.of(authorizedAction.getData());
    }
    return Optional.empty();
  }
}
