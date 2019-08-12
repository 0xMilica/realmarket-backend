package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.DigitalSignaturePrivateDto;
import io.realmarket.propeler.api.dto.DigitalSignaturePublicDto;

public interface DigitalSignatureService {
  DigitalSignaturePrivateDto getPrivateDigitalSignature();

  void save(DigitalSignaturePrivateDto digitalSignaturePrivateDto);

  DigitalSignaturePublicDto getPublicDigitalSignature(Long authId);
}
