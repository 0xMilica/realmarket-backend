package io.realmarket.propeler.service;

import io.realmarket.propeler.model.FundraisingProposal;
import io.realmarket.propeler.model.RegistrationToken;

public interface RegistrationTokenService {

  RegistrationToken createToken(FundraisingProposal fundraisingProposal);

  RegistrationToken findByValueAndNotExpiredOrThrowException(String value);

  void deleteToken(RegistrationToken registrationToken);
}
