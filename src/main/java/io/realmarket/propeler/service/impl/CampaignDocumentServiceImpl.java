package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignDocumentDto;
import io.realmarket.propeler.api.dto.CampaignDocumentResponseDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.CampaignDocument;
import io.realmarket.propeler.model.CampaignDocumentAccessLevel;
import io.realmarket.propeler.model.CampaignDocumentType;
import io.realmarket.propeler.model.enums.EUserRole;
import io.realmarket.propeler.repository.CampaignDocumentAccessLevelRepository;
import io.realmarket.propeler.repository.CampaignDocumentRepository;
import io.realmarket.propeler.repository.CampaignDocumentTypeRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignDocumentService;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Transactional
@Service
public class CampaignDocumentServiceImpl implements CampaignDocumentService {

  private final CampaignDocumentRepository campaignDocumentRepository;
  private final CampaignDocumentAccessLevelRepository campaignDocumentAccessLevelRepository;
  private final CampaignDocumentTypeRepository campaignDocumentTypeRepository;
  private final CampaignService campaignService;
  private final CloudObjectStorageService cloudObjectStorageService;
  private ModelMapperBlankString modelMapperBlankString;

  @Autowired
  public CampaignDocumentServiceImpl(
      CampaignDocumentRepository campaignDocumentRepository,
      CampaignDocumentAccessLevelRepository campaignDocumentAccessLevelRepository,
      CampaignDocumentTypeRepository campaignDocumentTypeRepository,
      CampaignService campaignService,
      CloudObjectStorageService cloudObjectStorageService,
      ModelMapperBlankString modelMapperBlankString) {
    this.campaignDocumentRepository = campaignDocumentRepository;
    this.campaignDocumentAccessLevelRepository = campaignDocumentAccessLevelRepository;
    this.campaignDocumentTypeRepository = campaignDocumentTypeRepository;
    this.campaignService = campaignService;
    this.cloudObjectStorageService = cloudObjectStorageService;
    this.modelMapperBlankString = modelMapperBlankString;
  }

  @Transactional
  public CampaignDocument submitDocument(
      CampaignDocumentDto campaignDocumentDto, String campaignUrlFriendlyName) {
    Campaign campaign =
        campaignService.findByUrlFriendlyNameOrThrowException(campaignUrlFriendlyName);
    campaignService.throwIfNotOwnerOrNotEditable(campaign);

    CampaignDocument campaignDocument = convertDocumentDtoToDocument(campaignDocumentDto, campaign);

    if (!cloudObjectStorageService.doesFileExist(campaignDocument.getUrl())) {
      throw new EntityNotFoundException(ExceptionMessages.FILE_NOT_EXISTS);
    }

    return campaignDocumentRepository.save(campaignDocument);
  }

  @Transactional
  public void deleteDocument(String campaignUrlFriendlyName, Long documentId) {

    CampaignDocument campaignDocument = findByIdOrThrowException(documentId);
    if (!campaignDocument.getCampaign().getUrlFriendlyName().equals(campaignUrlFriendlyName)) {
      throw new EntityNotFoundException(ExceptionMessages.FILE_NOT_EXISTS);
    }
    campaignService.throwIfNotOwnerOrNotEditable(campaignDocument.getCampaign());
    cloudObjectStorageService.delete(campaignDocument.getUrl());
    campaignDocumentRepository.delete(campaignDocument);
  }

  public boolean hasReadAccess(CampaignDocument campaignDocument) {
    if (campaignService.isOwner(campaignDocument.getCampaign())) {
      return true;
    }
    EUserRole eUserRole = AuthenticationUtil.getAuthentication().getAuth().getUserRole().getName();
    switch (campaignDocument.getAccessLevel().getName()) {
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

  public Map<String, List<CampaignDocumentResponseDto>> getAllCampaignDocumentDtoGropedByType(
      String campaignName) {
    Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    campaignService.throwIfNoAccess(campaign);

    return campaignDocumentRepository.findAllByCampaign(campaign).stream()
        .filter(this::hasReadAccess)
        .map(CampaignDocumentResponseDto::new)
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

  @Override
  public List<CampaignDocument> findAllByCampaign(Campaign campaign) {
    return campaignDocumentRepository.findAllByCampaign(campaign);
  }

  @Override
  public CampaignDocument patchCampaignDocument(
      String campaignUrlFriendlyName, Long documentId, CampaignDocumentDto campaignDocumentDto) {
    CampaignDocument campaignDocument = findByIdOrThrowException(documentId);
    if (!campaignDocument.getCampaign().getUrlFriendlyName().equals(campaignUrlFriendlyName)) {
      throw new EntityNotFoundException(ExceptionMessages.FILE_NOT_EXISTS);
    }
    campaignService.throwIfNotOwnerOrNotEditable(campaignDocument.getCampaign());

    CampaignDocument campaignDocumentPatch =
        convertDocumentDtoToDocument(campaignDocumentDto, campaignDocument.getCampaign());

    if (!cloudObjectStorageService.doesFileExist(campaignDocumentPatch.getUrl())) {
      throw new EntityNotFoundException(ExceptionMessages.FILE_NOT_EXISTS);
    }

    modelMapperBlankString.map(campaignDocumentPatch, campaignDocument);
    return campaignDocumentRepository.save(campaignDocument);
  }

  public CampaignDocument convertDocumentDtoToDocument(
      CampaignDocumentDto campaignDocumentDto, Campaign campaign) {
    Optional<CampaignDocumentAccessLevel> accessLevel =
        this.campaignDocumentAccessLevelRepository.findByName(campaignDocumentDto.getAccessLevel());
    Optional<CampaignDocumentType> type =
        this.campaignDocumentTypeRepository.findByName(campaignDocumentDto.getType());
    if (!accessLevel.isPresent() || !type.isPresent()) {
      throw new BadRequestException(ExceptionMessages.INVALID_REQUEST);
    }

    CampaignDocument campaignDocument =
        CampaignDocument.builder()
            .title(campaignDocumentDto.getTitle())
            .accessLevel(accessLevel.get())
            .type(type.get())
            .url(campaignDocumentDto.getUrl())
            .campaign(campaign)
            .uploadDate(Instant.now())
            .build();

    return campaignDocument;
  }
}
