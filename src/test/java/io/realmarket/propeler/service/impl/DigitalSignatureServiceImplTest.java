package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.DigitalSignaturePrivateDto;
import io.realmarket.propeler.api.dto.DigitalSignaturePublicDto;
import io.realmarket.propeler.repository.DigitalSignatureRepository;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.util.AuthUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static io.realmarket.propeler.util.AuthUtils.mockRequestAndContext;
import static io.realmarket.propeler.util.DigitalSignatureUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DigitalSignatureServiceImpl.class)
public class DigitalSignatureServiceImplTest {

  @InjectMocks DigitalSignatureServiceImpl digitalSignatureService;

  @Mock DigitalSignatureRepository digitalSignatureRepository;

  @Mock AuthService authService;

  @Before
  public void createAuthContext() {
    mockRequestAndContext();
  }

  @Test
  public void getPrivateDigitalSignature_Should_Return_Digital_Signature() {
    when(digitalSignatureRepository.getPrivateDigitalSignatureByAuth(AuthUtils.TEST_AUTH))
        .thenReturn(Optional.of(TEST_DIGITAL_SIGNATURE));

    DigitalSignaturePrivateDto actualDigitalSignaturePrivateDto =
        digitalSignatureService.getPrivateDigitalSignature();

    assertEquals(
        TEST_DIGITAL_SIGNATURE.getEncryptedPrivateKey(),
        actualDigitalSignaturePrivateDto.getEncryptedPrivateKey());
  }

  @Test(expected = EntityNotFoundException.class)
  public void getPrivateDigitalSignature_Should_Throw_Entity_Not_Found() {
    when(digitalSignatureRepository.getPrivateDigitalSignatureByAuth(AuthUtils.TEST_AUTH))
        .thenThrow(EntityNotFoundException.class);

    digitalSignatureService.getPrivateDigitalSignature();
  }

  @Test
  public void save_Should_Save_Digital_Signature() {
    when(digitalSignatureRepository.save(TEST_DIGITAL_SIGNATURE))
        .thenReturn(TEST_DIGITAL_SIGNATURE);
    digitalSignatureService.save(TEST_DIGITAL_SIGNATURE_PRIVATE_DTO);

    verify(digitalSignatureRepository, times(1)).save(any());
  }

  @Test
  public void getPublicDigitalSignature_Should_Return_Digital_Signature() {
    when(authService.findByIdOrThrowException(AuthUtils.TEST_AUTH_ID))
        .thenReturn(AuthUtils.TEST_AUTH);
    when(digitalSignatureRepository.getPublicDigitalSignatureByAuth(AuthUtils.TEST_AUTH))
        .thenReturn(Optional.of(TEST_DIGITAL_SIGNATURE_PUBLIC_DTO));

    DigitalSignaturePublicDto actualDigitalSignaturePublicDto =
        digitalSignatureService.getPublicDigitalSignature(AuthUtils.TEST_AUTH_ID);

    assertEquals(
        TEST_DIGITAL_SIGNATURE_PUBLIC_DTO.getPublicKey(),
        actualDigitalSignaturePublicDto.getPublicKey());
  }

  @Test(expected = EntityNotFoundException.class)
  public void getPublicDigitalSignature_Should_Throw_Entity_Not_Found() {
    when(authService.findByIdOrThrowException(AuthUtils.TEST_AUTH_ID))
        .thenReturn(AuthUtils.TEST_AUTH);
    when(digitalSignatureRepository.getPrivateDigitalSignatureByAuth(AuthUtils.TEST_AUTH))
        .thenThrow(EntityNotFoundException.class);

    digitalSignatureService.getPublicDigitalSignature(AuthUtils.TEST_AUTH_ID);
  }
}
