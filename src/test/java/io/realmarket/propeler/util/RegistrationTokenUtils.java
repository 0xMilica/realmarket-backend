package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.RegistrationTokenDto;
import io.realmarket.propeler.model.RegistrationToken;

import java.time.Instant;

public class RegistrationTokenUtils {

  public static final Long TEST_ID = 1L;
  public static final String TEST_VALUE = "value";
  public static final Instant TEST_EXPIRATION_TIME = Instant.now();

  public static final RegistrationToken TEST_REGISTRATION_TOKEN =
      RegistrationToken.builder()
          .id(TEST_ID)
          .value(TEST_VALUE)
          .expirationTime(TEST_EXPIRATION_TIME)
          .fundraisingProposal(FundraisingProposalUtils.TEST_APPROVED_FUNDRAISING_PROPOSAL)
          .build();

  public static final RegistrationTokenDto TEST_REGISTRATION_TOKEN_DTO =
      RegistrationTokenDto.builder().value(TEST_VALUE).build();
}
