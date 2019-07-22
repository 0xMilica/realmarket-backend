package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.FundraisingProposalDocument;
import io.realmarket.propeler.repository.DocumentTypeRepository;
import io.realmarket.propeler.repository.FundraisingProposalDocumentRepository;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.FundraisingProposalService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.util.CompanyDocumentUtils;
import io.realmarket.propeler.util.FundraisingProposalUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static io.realmarket.propeler.util.FundraisingProposalDocumentUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class FundraisingProposalDocumentServiceImplTest {

  @Mock private FundraisingProposalService fundraisingProposalService;
  @Mock private FundraisingProposalDocumentRepository fundraisingProposalDocumentRepository;
  @Mock private DocumentTypeRepository companyDocumentTypeRepository;
  @Mock private CloudObjectStorageService cloudObjectStorageService;

  @InjectMocks private FundraisingProposalDocumentServiceImpl fundraisingProposalDocumentService;

  @Test
  public void findByIdOrThrowException_Should_ReturnCampaignDocument() {
    when(fundraisingProposalDocumentRepository.findById(CompanyDocumentUtils.TEST_ID))
        .thenReturn(Optional.of(TEST_FUNDRAISING_PROPOSAL_DOCUMENT));

    FundraisingProposalDocument retVal =
        fundraisingProposalDocumentService.findByIdOrThrowException(TEST_ID);

    assertEquals(TEST_FUNDRAISING_PROPOSAL_DOCUMENT, retVal);
  }

  @Test(expected = EntityNotFoundException.class)
  public void findByIdOrThrowException_Should_EntityNotFoundException() {
    when(fundraisingProposalDocumentRepository.findById(TEST_ID)).thenReturn(Optional.empty());

    fundraisingProposalDocumentService.findByIdOrThrowException(TEST_ID);
  }

  @Test
  public void submitDocument_Should_CreateNewDocument() {
    when(fundraisingProposalService.findByIdOrThrowException(FundraisingProposalUtils.TEST_ID))
        .thenReturn(FundraisingProposalUtils.TEST_PENDING_FUNDRAISING_PROPOSAL);
    when(companyDocumentTypeRepository.findByName(TEST_TYPE_ENUM))
        .thenReturn(Optional.of(TEST_TYPE));
    when(cloudObjectStorageService.doesFileExist(TEST_FUNDRAISING_PROPOSAL_DOCUMENT_DTO.getUrl()))
        .thenReturn(true);
    when(fundraisingProposalDocumentRepository.save(any()))
        .thenReturn(TEST_FUNDRAISING_PROPOSAL_DOCUMENT);

    FundraisingProposalDocument retVal =
        fundraisingProposalDocumentService.submitDocument(
            TEST_FUNDRAISING_PROPOSAL_DOCUMENT_DTO, FundraisingProposalUtils.TEST_ID);

    assertEquals(TEST_FUNDRAISING_PROPOSAL_DOCUMENT, retVal);
  }

  @Test(expected = EntityNotFoundException.class)
  public void submitDocument_Should_Throw_FundraisingProposalNotExistsException() {
    when(fundraisingProposalService.findByIdOrThrowException(FundraisingProposalUtils.TEST_ID))
        .thenThrow(EntityNotFoundException.class);

    fundraisingProposalDocumentService.submitDocument(
        TEST_FUNDRAISING_PROPOSAL_DOCUMENT_DTO, FundraisingProposalUtils.TEST_ID);
  }

  @Test(expected = BadRequestException.class)
  public void submitDocument_Should_Throw_BadRequestException() {
    when(fundraisingProposalService.findByIdOrThrowException(FundraisingProposalUtils.TEST_ID))
        .thenReturn(FundraisingProposalUtils.TEST_PENDING_FUNDRAISING_PROPOSAL);

    fundraisingProposalDocumentService.submitDocument(
        TEST_FUNDRAISING_PROPOSAL_DOCUMENT_DTO, FundraisingProposalUtils.TEST_ID);
  }

  @Test(expected = EntityNotFoundException.class)
  public void submitDocument_Should_Throw_EntityNotFoundException() {
    when(fundraisingProposalService.findByIdOrThrowException(FundraisingProposalUtils.TEST_ID))
        .thenReturn(FundraisingProposalUtils.TEST_PENDING_FUNDRAISING_PROPOSAL);
    when(companyDocumentTypeRepository.findByName(TEST_TYPE_ENUM))
        .thenReturn(Optional.of(TEST_TYPE));
    when(cloudObjectStorageService.doesFileExist(TEST_FUNDRAISING_PROPOSAL_DOCUMENT_DTO.getUrl()))
        .thenReturn(false);

    fundraisingProposalDocumentService.submitDocument(
        TEST_FUNDRAISING_PROPOSAL_DOCUMENT_DTO, FundraisingProposalUtils.TEST_ID);
  }
}
