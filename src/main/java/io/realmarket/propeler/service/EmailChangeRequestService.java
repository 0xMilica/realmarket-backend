package io.realmarket.propeler.service;

import io.realmarket.propeler.model.EmailChangeRequest;
import io.realmarket.propeler.model.TemporaryToken;

public interface EmailChangeRequestService {
  void save(final EmailChangeRequest emailChangeRequest);

  EmailChangeRequest findByTokenOrThrowException(final TemporaryToken token);
}
