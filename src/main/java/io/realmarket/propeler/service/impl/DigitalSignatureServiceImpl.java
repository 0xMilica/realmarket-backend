package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.DigitalSignaturePrivateDto;
import io.realmarket.propeler.api.dto.DigitalSignaturePublicDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.DigitalSignature;
import io.realmarket.propeler.repository.DigitalSignatureRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.DigitalSignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.DIGITAL_SIGNATURE_DOES_NOT_EXIST;

@Service
public class DigitalSignatureServiceImpl implements DigitalSignatureService {

  private final DigitalSignatureRepository digitalSignatureRepository;
  private final AuthService authService;

  @Autowired
  public DigitalSignatureServiceImpl(
      DigitalSignatureRepository digitalSignatureRepository, AuthService authService) {
    this.digitalSignatureRepository = digitalSignatureRepository;
    this.authService = authService;
  }

  @Override
  public DigitalSignaturePrivateDto getPrivateDigitalSignature() {
    Auth auth = AuthenticationUtil.getAuthentication().getAuth();
    return new DigitalSignaturePrivateDto(getPrivateDigitalSignatureOrThrowException(auth));
  }

  @Override
  public void save(DigitalSignaturePrivateDto digitalSignaturePrivateDto) {
    DigitalSignature digitalSignature = new DigitalSignature(digitalSignaturePrivateDto);
    digitalSignature.setAuth(AuthenticationUtil.getAuthentication().getAuth());
    digitalSignatureRepository.save(digitalSignature);
  }

  @Override
  public DigitalSignaturePublicDto getPublicDigitalSignature(Long authId) {
    Auth auth = authService.findByIdOrThrowException(authId);
    return getPublicDigitalSignatureOrThrowException(auth);
  }

  private DigitalSignature getPrivateDigitalSignatureOrThrowException(Auth auth) {
    return digitalSignatureRepository
        .getPrivateDigitalSignatureByAuth(auth)
        .orElseThrow(() -> new EntityNotFoundException(DIGITAL_SIGNATURE_DOES_NOT_EXIST));
  }

  private DigitalSignaturePublicDto getPublicDigitalSignatureOrThrowException(Auth auth) {
    return digitalSignatureRepository
        .getPublicDigitalSignatureByAuth(auth)
        .orElseThrow(() -> new EntityNotFoundException(DIGITAL_SIGNATURE_DOES_NOT_EXIST));
  }
}
