package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignDocumentDto;
import io.realmarket.propeler.api.dto.CampaignDocumentResponseDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignDocument;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.repository.CampaignDocumentRepository;
import io.realmarket.propeler.repository.DocumentAccessLevelRepository;
import io.realmarket.propeler.repository.DocumentTypeRepository;
import io.realmarket.propeler.service.CampaignDocumentsAccessRequestService;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.CompanyService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import io.realmarket.propeler.util.AuthUtils;
import io.realmarket.propeler.util.CampaignDocumentUtils;
import io.realmarket.propeler.util.CampaignUtils;
import io.realmarket.propeler.util.CompanyUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.realmarket.propeler.util.AuthUtils.TEST_USER_AUTH2;
import static io.realmarket.propeler.util.AuthUtils.mockSecurityContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class CampaignDocumentServiceImplTest {

  @Mock private CampaignDocumentRepository campaignDocumentRepository;
  @Mock private DocumentAccessLevelRepository documentAccessLevelRepository;
  @Mock private DocumentTypeRepository documentTypeRepository;
  @Mock private CampaignService campaignService;
  @Mock private CompanyService companyService;
  @Mock private CampaignDocumentsAccessRequestService campaignDocumentsAccessRequestService;
  @Mock private CloudObjectStorageService cloudObjectStorageService;
  @Mock private ModelMapperBlankString modelMapperBlankString;

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
    when(documentAccessLevelRepository.findByName(CampaignDocumentUtils.TEST_ACCESS_LEVEL_ENUM))
        .thenReturn(Optional.of(CampaignDocumentUtils.TEST_ACCESS_LEVEL));
    when(documentTypeRepository.findByName(CampaignDocumentUtils.TEST_TYPE_ENUM))
        .thenReturn(Optional.of(CampaignDocumentUtils.TEST_TYPE));
    when(cloudObjectStorageService.doesFileExist(campaignDocumentDtoMocked.getUrl()))
        .thenReturn(true);
    when(campaignDocumentRepository.save(any())).thenReturn(campaignDocumentMocked);

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

    when(documentAccessLevelRepository.findByName(any()))
        .thenReturn(Optional.of(CampaignDocumentUtils.TEST_ACCESS_LEVEL));
    when(documentTypeRepository.findByName(any()))
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
    when(documentAccessLevelRepository.findByName(any()))
        .thenReturn(Optional.of(CampaignDocumentUtils.TEST_ACCESS_LEVEL));
    when(documentTypeRepository.findByName(any()))
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

    campaignDocumentService.deleteDocument(CampaignDocumentUtils.TEST_ID);
  }

  @Test(expected = EntityNotFoundException.class)
  public void deleteDocument_Should_Throw_CampaignNotExistsException() {
    when(campaignService.findByUrlFriendlyNameOrThrowException(
            CampaignUtils.TEST_URL_FRIENDLY_NAME))
        .thenThrow(EntityNotFoundException.class);

    campaignDocumentService.deleteDocument(CampaignDocumentUtils.TEST_ID);
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

    campaignDocumentService.deleteDocument(CampaignDocumentUtils.TEST_ID);
  }

  @Test(expected = EntityNotFoundException.class)
  public void deleteDocument_Should_Throw_AccessDeniedException() {
    when(campaignService.findByUrlFriendlyNameOrThrowException(
            CampaignUtils.TEST_URL_FRIENDLY_NAME))
        .thenReturn(CampaignUtils.TEST_CAMPAIGN);

    mockSecurityContext(TEST_USER_AUTH2);

    campaignDocumentService.deleteDocument(CampaignDocumentUtils.TEST_ID);
  }

  @Test(expected = EntityNotFoundException.class)
  public void deleteDocument_Should_Throw_CampaignDocumentNotExistsException() {
    when(campaignService.findByUrlFriendlyNameOrThrowException(
            CampaignUtils.TEST_URL_FRIENDLY_NAME))
        .thenReturn(CampaignUtils.TEST_CAMPAIGN);
    when(campaignDocumentRepository.findById(CampaignDocumentUtils.TEST_ID))
        .thenThrow(EntityNotFoundException.class);

    campaignDocumentService.deleteDocument(CampaignDocumentUtils.TEST_ID);
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
        campaignDocumentService.getAllCampaignDocumentDtoGroupedByType(
            CampaignUtils.TEST_URL_FRIENDLY_NAME);

    assertEquals(3, gropedDocs.get(CampaignDocumentUtils.TEST_TYPE_ENUM.toString()).size());
  }

  @Test(expected = EntityNotFoundException.class)
  public void findByIdOrThrowException_Should_EntityNotFoundException() {
    when(campaignDocumentRepository.findById(CampaignDocumentUtils.TEST_ID))
        .thenReturn(Optional.empty());

    campaignDocumentService.findByIdOrThrowException(CampaignDocumentUtils.TEST_ID);
  }

  @Test
  public void patchCampaignDocument_Should_ModifyCampaignDocument() {
    CampaignDocumentDto campaignDocumentDtoMocked =
        CampaignDocumentUtils.getCampaignDocumentDtoMocked2();
    CampaignDocument campaignDocumentMocked = CampaignDocumentUtils.getCampaignDocumentMocked2();

    when(campaignDocumentRepository.findById(CampaignDocumentUtils.TEST_ID))
        .thenReturn(Optional.of(CampaignDocumentUtils.TEST_CAMPAIGN_DOCUMENT));
    when(documentAccessLevelRepository.findByName(any()))
        .thenReturn(Optional.of(CampaignDocumentUtils.TEST_ACCESS_LEVEL_2));
    when(documentTypeRepository.findByName(any()))
        .thenReturn(Optional.of(CampaignDocumentUtils.TEST_TYPE_2));
    when(cloudObjectStorageService.doesFileExist(campaignDocumentDtoMocked.getUrl()))
        .thenReturn(true);
    when(campaignDocumentRepository.save(any())).thenReturn(campaignDocumentMocked);

    doAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              ((CampaignDocument) args[1]).setTitle(CampaignDocumentUtils.TEST_TITLE_2);
              ((CampaignDocument) args[1])
                  .setAccessLevel(CampaignDocumentUtils.TEST_ACCESS_LEVEL_2);
              ((CampaignDocument) args[1]).setType(CampaignDocumentUtils.TEST_TYPE_2);
              ((CampaignDocument) args[1]).setUrl(CampaignDocumentUtils.TEST_URL_2);
              return null;
            })
        .when(modelMapperBlankString)
        .map(campaignDocumentDtoMocked, campaignDocumentMocked);

    CampaignDocument retVal =
        campaignDocumentService.patchCampaignDocument(
            CampaignDocumentUtils.TEST_ID, campaignDocumentDtoMocked);

    assertEquals(campaignDocumentMocked, retVal);
  }

  @Test(expected = EntityNotFoundException.class)
  public void patchCampaignDocument_Should_Throw_EntityNotFoundException() {
    CampaignDocumentDto campaignDocumentDtoMocked =
        CampaignDocumentUtils.getCampaignDocumentDtoMocked2();

    campaignDocumentService.patchCampaignDocument(
        CampaignDocumentUtils.TEST_ID, campaignDocumentDtoMocked);
  }

  @Test(expected = BadRequestException.class)
  public void patchCampaignDocument_Should_Throw_BadRequestException() {
    CampaignDocumentDto campaignDocumentDtoMocked =
        CampaignDocumentUtils.getCampaignDocumentDtoMocked2();
    CampaignDocument campaignDocumentMocked = CampaignDocumentUtils.getCampaignDocumentMocked2();

    PowerMockito.when(campaignDocumentRepository.findById(CampaignDocumentUtils.TEST_ID))
        .thenReturn(Optional.of(campaignDocumentMocked));

    campaignDocumentService.patchCampaignDocument(
        CampaignDocumentUtils.TEST_ID, campaignDocumentDtoMocked);
  }

  @Test
  public void getUserDocuments_Should_ReturnListOfDocuments() {
    Company companyMocked = CompanyUtils.getCompanyMocked();
    Campaign campaignMocked = CampaignUtils.getCampaignMocked();

    when(companyService.findByAuthIdOrThrowException(AuthUtils.TEST_AUTH_ID))
        .thenReturn(companyMocked);
    when(campaignService.findAllByCompany(companyMocked)).thenReturn(Arrays.asList(campaignMocked));

    assertNotNull(campaignDocumentService.getUserCampaignDocuments(AuthUtils.TEST_AUTH_ID));
  }

  @Test
  public void getPageableUserDocuments_Should_ReturnListOfDocuments() {
    Company companyMocked = CompanyUtils.getCompanyMocked();
    Campaign campaignMocked = CampaignUtils.getCampaignMocked();
    Pageable pageable = Mockito.mock(Pageable.class);
    CampaignDocument campaignDocumentMocked = CampaignDocumentUtils.getCampaignDocumentMocked2();
    Page<CampaignDocument> page = new PageImpl<>(Arrays.asList(campaignDocumentMocked));

    when(companyService.findByAuthIdOrThrowException(AuthUtils.TEST_AUTH_ID))
        .thenReturn(companyMocked);
    when(campaignService.findAllByCompany(companyMocked)).thenReturn(Arrays.asList(campaignMocked));
    when(campaignDocumentRepository.findAllByCampaignIn(Arrays.asList(campaignMocked), pageable))
        .thenReturn(page);

    assertNotNull(
        campaignDocumentService.getPageableUserCampaignDocuments(AuthUtils.TEST_AUTH_ID, pageable));
  }
}
