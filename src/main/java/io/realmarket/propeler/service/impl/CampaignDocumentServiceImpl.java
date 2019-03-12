package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignDocument;
import io.realmarket.propeler.repository.CampaignDocumentRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignDocumentService;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Transactional
@Service
public class CampaignDocumentServiceImpl implements CampaignDocumentService {

  private final CampaignDocumentRepository campaignDocumentRepository;
  private final CampaignService campaignService;
  private final CloudObjectStorageService cloudObjectStorageService;

  @Autowired
  public CampaignDocumentServiceImpl(
      CampaignDocumentRepository campaignDocumentRepository,
      CampaignService campaignService,
      CloudObjectStorageService cloudObjectStorageService) {
    this.campaignDocumentRepository = campaignDocumentRepository;
    this.campaignService = campaignService;
    this.cloudObjectStorageService = cloudObjectStorageService;
  }

  @Transactional
  public CampaignDocument submitDocument(
      CampaignDocument campaignDocument, String campaignUrlFriendlyName) {
    Campaign campaign =
        campaignService.findByUrlFriendlyNameOrThrowException(campaignUrlFriendlyName);

    if (!AuthenticationUtil.isAuthenticatedUserId(campaign.getCompany().getAuth().getId())) {
      throw new AccessDeniedException(ExceptionMessages.NOT_CAMPAIGN_COMPANY_OWNER);
    }

    if (!cloudObjectStorageService.doesFileExist(campaignDocument.getUrl())) {
      throw new EntityNotFoundException(ExceptionMessages.FILE_NOT_EXISTS);
    }

    campaignDocument.setCampaign(campaign);

    return campaignDocumentRepository.save(campaignDocument);
  }

  @Transactional
  public void deleteDocument(String campaignUrlFriendlyName, Long documentId) {
    Campaign campaign =
        campaignService.findByUrlFriendlyNameOrThrowException(campaignUrlFriendlyName);

    if (!AuthenticationUtil.isAuthenticatedUserId(campaign.getCompany().getAuth().getId())) {
      throw new AccessDeniedException(ExceptionMessages.NOT_CAMPAIGN_COMPANY_OWNER);
    }

    CampaignDocument campaignDocument = findByIdOrThrowException(documentId);
    cloudObjectStorageService.delete(campaignDocument.getUrl());
    campaignDocumentRepository.delete(campaignDocument);
  }

  public CampaignDocument findByIdOrThrowException(Long documentId) {
    return campaignDocumentRepository
        .findById(documentId)
        .orElseThrow(EntityNotFoundException::new);
  }
}
