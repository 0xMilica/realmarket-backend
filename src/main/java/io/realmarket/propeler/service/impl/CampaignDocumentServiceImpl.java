package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignDocumentDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignDocument;
import io.realmarket.propeler.model.enums.EUserRole;
import io.realmarket.propeler.repository.CampaignDocumentRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignDocumentService;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

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
    campaignService.throwIfNoAccess(campaign);

    if (!cloudObjectStorageService.doesFileExist(campaignDocument.getUrl())) {
      throw new EntityNotFoundException(ExceptionMessages.FILE_NOT_EXISTS);
    }

    campaignDocument.setCampaign(campaign);

    return campaignDocumentRepository.save(campaignDocument);
  }

  @Transactional
  public void deleteDocument(String campaignUrlFriendlyName, Long documentId) {

    CampaignDocument campaignDocument = findByIdOrThrowException(documentId);
    if (!campaignDocument.getCampaign().getUrlFriendlyName().equals(campaignUrlFriendlyName)) {
      throw new EntityNotFoundException(ExceptionMessages.FILE_NOT_EXISTS);
    }
    campaignService.throwIfNoAccess(campaignDocument.getCampaign());
    cloudObjectStorageService.delete(campaignDocument.getUrl());
    campaignDocumentRepository.delete(campaignDocument);
  }

  public boolean hasReadAccess(CampaignDocument campaignDocument) {
    if (campaignService.isOwner(campaignDocument.getCampaign())) {
      return true;
    }
    EUserRole eUserRole = AuthenticationUtil.getAuthentication().getAuth().getUserRole();
    switch (campaignDocument.getAccessLevel()) {
      case PUBLIC:
        return true;
      case INVESTORS:
        if (eUserRole.equals(EUserRole.ROLE_INVESTOR)) return true;
        // Access INVESTOR means that both ROLE_INVESTOR and ROLE_ADMIN can access this document.
        // because of this break is omitted.
      case PLATFORM_ADMINS:
        if (eUserRole.equals(EUserRole.ROLE_ADMIN)) return true;
      default:
        return false;
    }
  }

  public Map<String, List<CampaignDocumentDto>> getAllCampaignDocumentDtoGropedByType(
      String campaignName) {
    Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    campaignService.throwIfNoAccess(campaign);

    return campaignDocumentRepository.findAllByCampaign(campaign).stream()
        .filter(this::hasReadAccess)
        .map(CampaignDocumentDto::new)
        .collect(
            groupingBy(
                campaignDocumentDto -> {
                  return campaignDocumentDto.getType().toString();
                },
                toList()));
  }

  public CampaignDocument findByIdOrThrowException(Long documentId) {
    return campaignDocumentRepository
        .findById(documentId)
        .orElseThrow(EntityNotFoundException::new);
  }
}
