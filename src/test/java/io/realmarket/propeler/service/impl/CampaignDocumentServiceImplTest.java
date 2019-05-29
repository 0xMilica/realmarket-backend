package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignDocumentDto;
import io.realmarket.propeler.api.dto.CampaignDocumentResponseDto;
import io.realmarket.propeler.model.CampaignDocument;
import io.realmarket.propeler.repository.CampaignDocumentAccessLevelRepository;
import io.realmarket.propeler.repository.CampaignDocumentRepository;
import io.realmarket.propeler.repository.CampaignDocumentTypeRepository;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.util.AuthUtils;
import io.realmarket.propeler.util.CampaignDocumentUtils;
import io.realmarket.propeler.util.CampaignUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.realmarket.propeler.util.AuthUtils.TEST_USER_AUTH2;
import static io.realmarket.propeler.util.AuthUtils.mockSecurityContext;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class CampaignDocumentServiceImplTest {

  @Mock private CampaignDocumentRepository campaignDocumentRepository;
  @Mock private CampaignDocumentAccessLevelRepository campaignDocumentAccessLevelRepository;
  @Mock private CampaignDocumentTypeRepository campaignDocumentTypeRepository;
  @Mock private CampaignService campaignService;
  @Mock private CloudObjectStorageService cloudObjectStorageService;

  @InjectMocks private CampaignDocumentServiceImpl campaignDocumentService;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContext();
  }

  @Test
  public void submitDocument_Should_CreateNewDocument() {
    CampaignDocumentDto campaignDocumentDtoMocked =
        CampaignDocumentUtils.getCampaignDocumentDtoMocked();
    CampaignDocument campaignDocumentMocked = CampaignDocumentUtils.getCampaignDocumentMocked();

    when(campaignService.findByUrlFriendlyNameOrThrowException(
            CampaignUtils.TEST_URL_FRIENDLY_NAME))
        .thenReturn(CampaignUtils.TEST_CAMPAIGN);
    when(campaignDocumentAccessLevelRepository.findByName(any()))
        .thenReturn(Optional.of(CampaignDocumentUtils.TEST_ACCESS_LEVEL));
    when(campaignDocumentTypeRepository.findByName(any()))
        .thenReturn(Optional.of(CampaignDocumentUtils.TEST_TYPE));
    when(cloudObjectStorageService.doesFileExist(campaignDocumentDtoMocked.getUrl()))
        .thenReturn(true);
    when(campaignDocumentRepository.save(any())).thenReturn(campaignDocumentMocked);
    when(campaignDocumentService.submitDocument(
            campaignDocumentDtoMocked, CampaignUtils.TEST_URL_FRIENDLY_NAME))
        .thenReturn(campaignDocumentMocked);

    CampaignDocument retVal =
        campaignDocumentService.submitDocument(
            campaignDocumentDtoMocked, CampaignUtils.TEST_URL_FRIENDLY_NAME);

    assertEquals(campaignDocumentMocked, retVal);
  }

  @Test(expected = EntityNotFoundException.class)
  public void submitDocument_Should_Throw_CampaignNotExistsException() {
    CampaignDocumentDto campaignDocumentDtoMocked =
        CampaignDocumentUtils.getCampaignDocumentDtoMocked();

    when(campaignService.findByUrlFriendlyNameOrThrowException(
            CampaignUtils.TEST_URL_FRIENDLY_NAME))
        .thenThrow(EntityNotFoundException.class);

    campaignDocumentService.submitDocument(
        campaignDocumentDtoMocked, CampaignUtils.TEST_URL_FRIENDLY_NAME);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void submitDocument_Should_Throw_AccessDeniedException() {
    CampaignDocumentDto campaignDocumentDtoMocked =
        CampaignDocumentUtils.getCampaignDocumentDtoMocked();
    CampaignDocument campaignDocumentMocked = CampaignDocumentUtils.getCampaignDocumentMocked();

    when(campaignService.findByUrlFriendlyNameOrThrowException(
            CampaignUtils.TEST_URL_FRIENDLY_NAME))
        .thenReturn(CampaignUtils.TEST_CAMPAIGN);

    Mockito.doThrow(ForbiddenOperationException.class)
        .when(campaignService)
        .throwIfNotOwnerOrNotEditable(CampaignUtils.TEST_CAMPAIGN);

    when(campaignDocumentAccessLevelRepository.findByName(any()))
        .thenReturn(Optional.of(CampaignDocumentUtils.TEST_ACCESS_LEVEL));
    when(campaignDocumentTypeRepository.findByName(any()))
        .thenReturn(Optional.of(CampaignDocumentUtils.TEST_TYPE));
    when(cloudObjectStorageService.doesFileExist(campaignDocumentMocked.getUrl())).thenReturn(true);
    when(campaignDocumentRepository.save(campaignDocumentMocked))
        .thenReturn(campaignDocumentMocked);

    mockSecurityContext(TEST_USER_AUTH2);

    campaignDocumentService.submitDocument(
        campaignDocumentDtoMocked, CampaignUtils.TEST_URL_FRIENDLY_NAME);
  }

  @Test(expected = EntityNotFoundException.class)
  public void submitDocument_Should_Throw_EntityNotFoundException() {
    CampaignDocumentDto campaignDocumentDtoMocked =
        CampaignDocumentUtils.getCampaignDocumentDtoMocked();
    CampaignDocument campaignDocumentMocked = CampaignDocumentUtils.getCampaignDocumentMocked();

    when(campaignService.findByUrlFriendlyNameOrThrowException(
            CampaignUtils.TEST_URL_FRIENDLY_NAME))
        .thenReturn(CampaignUtils.TEST_CAMPAIGN);
    when(campaignDocumentAccessLevelRepository.findByName(any()))
        .thenReturn(Optional.of(CampaignDocumentUtils.TEST_ACCESS_LEVEL));
    when(campaignDocumentTypeRepository.findByName(any()))
        .thenReturn(Optional.of(CampaignDocumentUtils.TEST_TYPE));
    when(cloudObjectStorageService.doesFileExist(campaignDocumentMocked.getUrl()))
        .thenReturn(false);
    when(campaignDocumentRepository.save(campaignDocumentMocked))
        .thenReturn(campaignDocumentMocked);

    campaignDocumentService.submitDocument(
        campaignDocumentDtoMocked, CampaignUtils.TEST_URL_FRIENDLY_NAME);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void submitDocument_ShouldThrow_ForbiddenOperationException() {
    CampaignDocumentDto campaignDocumentDtoMocked =
        CampaignDocumentUtils.getCampaignDocumentDtoMocked();

    when(campaignService.findByUrlFriendlyNameOrThrowException(
            CampaignUtils.TEST_ACTIVE_URL_FRIENDLY_NAME))
        .thenReturn(CampaignUtils.TEST_ACTIVE_CAMPAIGN);
    Mockito.doThrow(ForbiddenOperationException.class)
        .when(campaignService)
        .throwIfNotOwnerOrNotEditable(CampaignUtils.TEST_ACTIVE_CAMPAIGN);

    campaignDocumentService.submitDocument(
        campaignDocumentDtoMocked, CampaignUtils.TEST_ACTIVE_URL_FRIENDLY_NAME);
  }

  @Test
  public void deleteDocument_Should_RemoveDocument() {
    CampaignDocument campaignDocumentMocked = CampaignDocumentUtils.getCampaignDocumentMocked();

    when(campaignService.findByUrlFriendlyNameOrThrowException(
            CampaignUtils.TEST_URL_FRIENDLY_NAME))
        .thenReturn(CampaignUtils.TEST_CAMPAIGN);
    when(campaignDocumentRepository.findById(CampaignDocumentUtils.TEST_ID))
        .thenReturn(Optional.of(campaignDocumentMocked));

    campaignDocumentService.deleteDocument(
        CampaignUtils.TEST_URL_FRIENDLY_NAME, CampaignDocumentUtils.TEST_ID);
  }

  @Test(expected = EntityNotFoundException.class)
  public void deleteDocument_Should_Throw_CampaignNotExistsException() {
    when(campaignService.findByUrlFriendlyNameOrThrowException(
            CampaignUtils.TEST_URL_FRIENDLY_NAME))
        .thenThrow(EntityNotFoundException.class);

    campaignDocumentService.deleteDocument(
        CampaignUtils.TEST_URL_FRIENDLY_NAME, CampaignDocumentUtils.TEST_ID);
  }

  @Test(expected = EntityNotFoundException.class)
  public void deleteDocument_Should_Throw_ForbiddenOperationException() {
    CampaignDocument campaignDocumentMocked = CampaignDocumentUtils.getCampaignDocumentMocked();
    campaignDocumentMocked.setCampaign(CampaignUtils.TEST_ACTIVE_CAMPAIGN);

    when(campaignService.findByUrlFriendlyNameOrThrowException(
            CampaignUtils.TEST_ACTIVE_URL_FRIENDLY_NAME))
        .thenReturn(CampaignUtils.TEST_ACTIVE_CAMPAIGN);

    Mockito.doThrow(ForbiddenOperationException.class)
        .when(campaignService)
        .throwIfNotEditable(CampaignUtils.TEST_ACTIVE_CAMPAIGN);

    campaignDocumentService.deleteDocument(
        CampaignUtils.TEST_ACTIVE_URL_FRIENDLY_NAME, CampaignDocumentUtils.TEST_ID);
  }

  @Test(expected = EntityNotFoundException.class)
  public void deleteDocument_Should_Throw_AccessDeniedException() {
    when(campaignService.findByUrlFriendlyNameOrThrowException(
            CampaignUtils.TEST_URL_FRIENDLY_NAME))
        .thenReturn(CampaignUtils.TEST_CAMPAIGN);

    mockSecurityContext(TEST_USER_AUTH2);

    campaignDocumentService.deleteDocument(
        CampaignUtils.TEST_URL_FRIENDLY_NAME, CampaignDocumentUtils.TEST_ID);
  }

  @Test(expected = EntityNotFoundException.class)
  public void deleteDocument_Should_Throw_CampaignDocumentNotExistsException() {
    when(campaignService.findByUrlFriendlyNameOrThrowException(
            CampaignUtils.TEST_URL_FRIENDLY_NAME))
        .thenReturn(CampaignUtils.TEST_CAMPAIGN);
    when(campaignDocumentRepository.findById(CampaignDocumentUtils.TEST_ID))
        .thenThrow(EntityNotFoundException.class);

    campaignDocumentService.deleteDocument(
        CampaignUtils.TEST_URL_FRIENDLY_NAME, CampaignDocumentUtils.TEST_ID);
  }

  @Test
  public void findByIdOrThrowException_Should_ReturnCampaignDocument() {
    CampaignDocument campaignDocumentMocked = CampaignDocumentUtils.getCampaignDocumentMocked();

    when(campaignDocumentRepository.findById(CampaignDocumentUtils.TEST_ID))
        .thenReturn(Optional.of(campaignDocumentMocked));

    CampaignDocument retVal =
        campaignDocumentService.findByIdOrThrowException(CampaignDocumentUtils.TEST_ID);

    assertEquals(campaignDocumentMocked, retVal);
  }

  @Test
  public void getAllCampaignDocumentDtoGropedByType() {
    when(campaignService.findByUrlFriendlyNameOrThrowException(
            CampaignUtils.TEST_URL_FRIENDLY_NAME))
        .thenReturn(CampaignUtils.TEST_CAMPAIGN);

    when(campaignDocumentRepository.findAllByCampaign(CampaignUtils.TEST_CAMPAIGN))
        .thenReturn(CampaignDocumentUtils.TEST_CAMPAIGN_DOCUMENT_LIST);

    Map<String, List<CampaignDocumentResponseDto>> gropedDocs =
        campaignDocumentService.getAllCampaignDocumentDtoGropedByType(
            CampaignUtils.TEST_URL_FRIENDLY_NAME);

    assertEquals(3, gropedDocs.get(CampaignDocumentUtils.TEST_TYPE_ENUM.toString()).size());
  }

  @Test(expected = EntityNotFoundException.class)
  public void findByIdOrThrowException_Should_EntityNotFoundException() {
    when(campaignDocumentRepository.findById(CampaignDocumentUtils.TEST_ID))
        .thenReturn(Optional.empty());

    campaignDocumentService.findByIdOrThrowException(CampaignDocumentUtils.TEST_ID);
  }
}
