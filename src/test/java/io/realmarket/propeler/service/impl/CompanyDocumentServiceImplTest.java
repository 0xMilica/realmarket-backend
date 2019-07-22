package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CompanyDocumentDto;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.CompanyDocument;
import io.realmarket.propeler.repository.CompanyDocumentRepository;
import io.realmarket.propeler.repository.DocumentAccessLevelRepository;
import io.realmarket.propeler.repository.DocumentTypeRepository;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.CompanyService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import io.realmarket.propeler.util.AuthUtils;
import io.realmarket.propeler.util.CampaignDocumentUtils;
import io.realmarket.propeler.util.CompanyDocumentUtils;
import io.realmarket.propeler.util.CompanyUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class CompanyDocumentServiceImplTest {

  @Mock private CompanyDocumentRepository companyDocumentRepository;
  @Mock private DocumentAccessLevelRepository documentAccessLevelRepository;
  @Mock private DocumentTypeRepository companyDocumentTypeRepository;
  @Mock private CompanyService companyService;
  @Mock private CloudObjectStorageService cloudObjectStorageService;
  @Mock private ModelMapperBlankString modelMapperBlankString;

  @InjectMocks private CompanyDocumentServiceImpl companyDocumentService;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContext();
  }

  @Test
  public void findByIdOrThrowException_Should_ReturnCampaignDocument() {
    CompanyDocument companyDocumentMocked = CompanyDocumentUtils.getCompanyDocumentMocked();

    when(companyDocumentRepository.findById(CompanyDocumentUtils.TEST_ID))
        .thenReturn(Optional.of(companyDocumentMocked));

    CompanyDocument retVal =
        companyDocumentService.findByIdOrThrowException(CompanyDocumentUtils.TEST_ID);

    assertEquals(companyDocumentMocked, retVal);
  }

  @Test(expected = EntityNotFoundException.class)
  public void findByIdOrThrowException_Should_EntityNotFoundException() {
    when(companyDocumentRepository.findById(CompanyDocumentUtils.TEST_ID))
        .thenReturn(Optional.empty());

    companyDocumentService.findByIdOrThrowException(CampaignDocumentUtils.TEST_ID);
  }

  @Test
  public void submitDocument_Should_CreateNewDocument() {
    CompanyDocument companyDocumentMocked = CompanyDocumentUtils.getCompanyDocumentMocked();
    CompanyDocumentDto companyDocumentDtoMocked =
        CompanyDocumentUtils.getCompanyDocumentDtoMocked();

    when(companyService.findByIdOrThrowException(CompanyUtils.TEST_ID))
        .thenReturn(CompanyUtils.TEST_COMPANY);
    when(documentAccessLevelRepository.findByName(CompanyDocumentUtils.TEST_ACCESS_LEVEL_ENUM))
        .thenReturn(Optional.of(CompanyDocumentUtils.TEST_ACCESS_LEVEL));
    when(companyDocumentTypeRepository.findByName(CompanyDocumentUtils.TEST_TYPE_ENUM))
        .thenReturn(Optional.of(CompanyDocumentUtils.TEST_TYPE));
    when(cloudObjectStorageService.doesFileExist(companyDocumentDtoMocked.getUrl()))
        .thenReturn(true);
    when(companyDocumentRepository.save(any())).thenReturn(companyDocumentMocked);

    CompanyDocument retVal =
        companyDocumentService.submitDocument(companyDocumentDtoMocked, CompanyUtils.TEST_ID);

    assertEquals(companyDocumentMocked, retVal);
  }

  @Test(expected = EntityNotFoundException.class)
  public void submitDocument_Should_Throw_CompanyNotExistsException() {
    CompanyDocumentDto companyDocumentDtoMocked =
        CompanyDocumentUtils.getCompanyDocumentDtoMocked();

    when(companyService.findByIdOrThrowException(CompanyUtils.TEST_ID))
        .thenThrow(EntityNotFoundException.class);

    companyDocumentService.submitDocument(companyDocumentDtoMocked, CompanyUtils.TEST_ID);
  }

  @Test(expected = BadRequestException.class)
  public void submitDocument_Should_Throw_BadRequestException() {
    CompanyDocumentDto companyDocumentDtoMocked =
        CompanyDocumentUtils.getCompanyDocumentDtoMocked();

    when(companyService.findByIdOrThrowException(CompanyUtils.TEST_ID))
        .thenReturn(CompanyUtils.TEST_COMPANY);

    companyDocumentService.submitDocument(companyDocumentDtoMocked, CompanyUtils.TEST_ID);
  }

  @Test(expected = EntityNotFoundException.class)
  public void submitDocument_Should_Throw_EntityNotFoundException() {
    CompanyDocumentDto companyDocumentDtoMocked =
        CompanyDocumentUtils.getCompanyDocumentDtoMocked();

    when(companyService.findByIdOrThrowException(CompanyUtils.TEST_ID))
        .thenReturn(CompanyUtils.TEST_COMPANY);
    when(documentAccessLevelRepository.findByName(CompanyDocumentUtils.TEST_ACCESS_LEVEL_ENUM))
        .thenReturn(Optional.of(CompanyDocumentUtils.TEST_ACCESS_LEVEL));
    when(companyDocumentTypeRepository.findByName(CompanyDocumentUtils.TEST_TYPE_ENUM))
        .thenReturn(Optional.of(CompanyDocumentUtils.TEST_TYPE));
    when(cloudObjectStorageService.doesFileExist(companyDocumentDtoMocked.getUrl()))
        .thenReturn(false);

    companyDocumentService.submitDocument(companyDocumentDtoMocked, CompanyUtils.TEST_ID);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void submitDocument_Should_Throw_AccessDeniedException() {
    CompanyDocumentDto companyDocumentDtoMocked =
        CompanyDocumentUtils.getCompanyDocumentDtoMocked();

    // mockSecurityContext(TEST_USER_AUTH2);

    when(companyService.findByIdOrThrowException(CompanyUtils.TEST_ID))
        .thenReturn(CompanyUtils.TEST_COMPANY);

    Mockito.doThrow(ForbiddenOperationException.class)
        .when(companyService)
        .throwIfNoAccess(CompanyUtils.TEST_COMPANY);

    companyDocumentService.submitDocument(companyDocumentDtoMocked, CompanyUtils.TEST_ID);
  }

  @Test
  public void deleteDocument_Should_RemoveDocument() {
    CompanyDocument companyDocumentMocked = CompanyDocumentUtils.getCompanyDocumentMocked();

    when(companyService.findByIdOrThrowException(CompanyUtils.TEST_ID))
        .thenReturn(CompanyUtils.TEST_COMPANY);
    when(companyDocumentRepository.findById(CompanyDocumentUtils.TEST_ID))
        .thenReturn(Optional.of(companyDocumentMocked));

    companyDocumentService.deleteDocument(CompanyDocumentUtils.TEST_ID);
  }

  @Test(expected = EntityNotFoundException.class)
  public void deleteDocument_Should_Throw_CampaignNotExistsException() {
    companyDocumentService.deleteDocument(CompanyDocumentUtils.TEST_ID);
  }

  @Test(expected = ForbiddenOperationException.class)
  public void deleteDocument_Should_Throw_ForbiddenOperationException() {
    CompanyDocument companyDocumentMocked = CompanyDocumentUtils.getCompanyDocumentMocked();

    when(companyService.findByIdOrThrowException(CompanyUtils.TEST_ID))
        .thenReturn(CompanyUtils.TEST_COMPANY);
    when(companyDocumentRepository.findById(CompanyDocumentUtils.TEST_ID))
        .thenReturn(Optional.of(companyDocumentMocked));

    Mockito.doThrow(ForbiddenOperationException.class)
        .when(companyService)
        .throwIfNoAccess(CompanyUtils.TEST_COMPANY);

    companyDocumentService.deleteDocument(CompanyDocumentUtils.TEST_ID);
  }

  @Test
  public void patchDocument_Should_ModifyDocument() {
    CompanyDocument companyDocumentMocked = CompanyDocumentUtils.getCompanyDocumentMocked2();
    CompanyDocumentDto companyDocumentDtoMocked =
        CompanyDocumentUtils.getCompanyDocumentDtoMocked2();

    when(companyDocumentRepository.findById(CompanyDocumentUtils.TEST_ID))
        .thenReturn(Optional.of(CompanyDocumentUtils.TEST_COMPANY_DOCUMENT));
    when(documentAccessLevelRepository.findByName(any()))
        .thenReturn(Optional.of(CompanyDocumentUtils.TEST_ACCESS_LEVEL_2));
    when(companyDocumentTypeRepository.findByName(any()))
        .thenReturn(Optional.of(CompanyDocumentUtils.TEST_TYPE_2));
    when(cloudObjectStorageService.doesFileExist(companyDocumentDtoMocked.getUrl()))
        .thenReturn(true);
    when(companyDocumentRepository.save(any())).thenReturn(companyDocumentMocked);

    doAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              ((CompanyDocument) args[1]).setTitle(CompanyDocumentUtils.TEST_TITLE_2);
              ((CompanyDocument) args[1]).setAccessLevel(CompanyDocumentUtils.TEST_ACCESS_LEVEL_2);
              ((CompanyDocument) args[1]).setType(CompanyDocumentUtils.TEST_TYPE_2);
              ((CompanyDocument) args[1]).setUrl(CompanyDocumentUtils.TEST_URL_2);
              return null;
            })
        .when(modelMapperBlankString)
        .map(companyDocumentDtoMocked, companyDocumentMocked);

    CompanyDocument retVal =
        companyDocumentService.patchCompanyDocument(
            CompanyDocumentUtils.TEST_ID, companyDocumentDtoMocked);

    assertEquals(companyDocumentMocked, retVal);
  }

  @Test(expected = EntityNotFoundException.class)
  public void patchDocument_Should_Throw_EntityNotFoundException() {
    CompanyDocumentDto companyDocumentDtoMocked =
        CompanyDocumentUtils.getCompanyDocumentDtoMocked2();

    companyDocumentService.patchCompanyDocument(
        CompanyDocumentUtils.TEST_ID, companyDocumentDtoMocked);
  }

  @Test(expected = BadRequestException.class)
  public void patchDocument_Should_Throw_BadRequestException() {
    CompanyDocumentDto companyDocumentDtoMocked =
        CompanyDocumentUtils.getCompanyDocumentDtoMocked2();

    when(companyDocumentRepository.findById(CompanyDocumentUtils.TEST_ID))
        .thenReturn(Optional.of(CompanyDocumentUtils.TEST_COMPANY_DOCUMENT));

    companyDocumentService.patchCompanyDocument(
        CompanyDocumentUtils.TEST_ID, companyDocumentDtoMocked);
  }

  @Test
  public void getUserDocuments_Should_ReturnListOfDocuments() {
    Company companyMocked = CompanyUtils.getCompanyMocked();
    CompanyDocument companyDocumentMocked = CompanyDocumentUtils.getCompanyDocumentMocked();

    when(companyService.findByAuthIdOrThrowException(AuthUtils.TEST_AUTH_ID))
        .thenReturn(companyMocked);
    when(companyDocumentRepository.findAllByCompany(companyMocked))
        .thenReturn(Arrays.asList(companyDocumentMocked));

    assertNotNull(companyDocumentService.getUserCompanyDocuments(AuthUtils.TEST_AUTH_ID));
  }
}
