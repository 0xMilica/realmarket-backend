package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.EmailChangeRequest;
import io.realmarket.propeler.model.TemporaryToken;
import io.realmarket.propeler.repository.EmailChangeRequestRepository;
import io.realmarket.propeler.service.EmailChangeRequestService;
import io.realmarket.propeler.service.TemporaryTokenService;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
public class EmailChangeRequestServiceImpl implements EmailChangeRequestService {

  private EmailChangeRequestRepository emailChangeRequestRepository;

  @Autowired
  public EmailChangeRequestServiceImpl(
      EmailChangeRequestRepository emailChangeRequestRepository,
      TemporaryTokenService temporaryTokenService) {
    this.emailChangeRequestRepository = emailChangeRequestRepository;
  }

  public void save(EmailChangeRequest emailChangeRequest) {
    emailChangeRequestRepository.save(emailChangeRequest);
  }

  public EmailChangeRequest findByTokenOrThrowException(final TemporaryToken token) {
    return emailChangeRequestRepository
        .findByTemporaryToken(token)
        .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.INVALID_TOKEN_PROVIDED));
  }
}
