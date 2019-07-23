package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CampaignDocumentDto;
import io.realmarket.propeler.api.dto.CampaignDocumentResponseDto;
import io.realmarket.propeler.model.*;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.CampaignDocumentRepository;
import io.realmarket.propeler.repository.DocumentAccessLevelRepository;
import io.realmarket.propeler.repository.DocumentTypeRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignDocumentService;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.CompanyService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Transactional
@Service
public class CampaignDocumentServiceImpl implements CampaignDocumentService {

  private final CampaignDocumentRepository campaignDocumentRepository;
  private final DocumentAccessLevelRepository documentAccessLevelRepository;
  private final DocumentTypeRepository documentTypeRepository;
  private final CampaignService campaignService;
  private final CompanyService companyService;
  private final CloudObjectStorageService cloudObjectStorageService;
  private final ModelMapperBlankString modelMapperBlankString;

  @Autowired
  public CampaignDocumentServiceImpl(
      CampaignDocumentRepository campaignDocumentRepository,
      DocumentAccessLevelRepository documentAccessLevelRepository,
      DocumentTypeRepository documentTypeRepository,
      CampaignService campaignService,
      CompanyService companyService,
      CloudObjectStorageService cloudObjectStorageService,
      ModelMapperBlankString modelMapperBlankString) {
    this.campaignDocumentRepository = campaignDocumentRepository;
    this.documentAccessLevelRepository = documentAccessLevelRepository;
    this.documentTypeRepository = documentTypeRepository;
    this.campaignService = campaignService;
    this.companyService = companyService;
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
    campaignDocument.setUploadDate(Instant.now());

    if (!cloudObjectStorageService.doesFileExist(campaignDocument.getUrl())) {
      throw new EntityNotFoundException(ExceptionMessages.FILE_DOES_NOT_EXIST);
    }

    return campaignDocumentRepository.save(campaignDocument);
  }

  @Transactional
  public void deleteDocument(Long documentId) {

    CampaignDocument campaignDocument = findByIdOrThrowException(documentId);

    campaignService.throwIfNotOwnerOrNotEditable(campaignDocument.getCampaign());
    cloudObjectStorageService.delete(campaignDocument.getUrl());
    campaignDocumentRepository.delete(campaignDocument);
  }

  public boolean hasReadAccess(CampaignDocument campaignDocument) {
    if (campaignService.isOwner(campaignDocument.getCampaign())) {
      return true;
    }
    UserRoleName userRoleName =
        AuthenticationUtil.getAuthentication().getAuth().getUserRole().getName();
    DocumentAccessLevel accessLevel = campaignDocument.getAccessLevel();
    return DocumentAccessLevel.hasReadAccess(accessLevel, userRoleName);
  }

  public Map<String, List<CampaignDocumentResponseDto>> getAllCampaignDocumentDtoGroupedByType(
      String campaignName) {
    Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);
    campaignService.throwIfNoAccess(campaign);

    return campaignDocumentRepository.findAllByCampaign(campaign).stream()
        .filter(this::hasReadAccess)
        .map(CampaignDocumentResponseDto::new)
        .collect(
            groupingBy(campaignDocumentDto -> campaignDocumentDto.getType().toString(), toList()));
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
  public List<CampaignDocument> findAllByCampaigns(List<Campaign> campaigns) {
    return campaignDocumentRepository.findAllByCampaignIn(campaigns);
  }

  @Override
  public Page<CampaignDocument> findAllPageableByCampaigns(
      List<Campaign> campaigns, Pageable pageable) {
    return campaignDocumentRepository.findAllByCampaignIn(campaigns, pageable);
  }

  @Override
  public List<CampaignDocument> findAllByCampaignOrderByUploadDateDesc(Campaign campaign) {
    return campaignDocumentRepository.findAllByCampaignOrderByUploadDateDesc(campaign);
  }

  @Override
  public CampaignDocument patchCampaignDocument(
      Long documentId, CampaignDocumentDto campaignDocumentDto) {
    CampaignDocument campaignDocument = findByIdOrThrowException(documentId);

    campaignService.throwIfNotOwnerOrNotEditable(campaignDocument.getCampaign());

    CampaignDocument campaignDocumentPatch =
        convertDocumentDtoToDocument(campaignDocumentDto, campaignDocument.getCampaign());

    if (!cloudObjectStorageService.doesFileExist(campaignDocumentPatch.getUrl())) {
      throw new EntityNotFoundException(ExceptionMessages.FILE_DOES_NOT_EXIST);
    }

    modelMapperBlankString.map(campaignDocumentPatch, campaignDocument);
    return campaignDocumentRepository.save(campaignDocument);
  }

  @Override
  public List<CampaignDocumentResponseDto> getUserCampaignDocuments(Long userId) {
    Company company = companyService.findByAuthIdOrThrowException(userId);
    companyService.throwIfNotOwnerOrAdmin(
        company, AuthenticationUtil.getAuthentication().getAuth());

    List<Campaign> campaignsList = campaignService.findAllByCompany(company);

    return findAllByCampaigns(campaignsList).stream()
        .map(CampaignDocumentResponseDto::new)
        .collect(toList());
  }

  @Override
  public Page<CampaignDocumentResponseDto> getPageableUserCampaignDocuments(
      Long userId, Pageable pageable) {
    Company company = companyService.findByAuthIdOrThrowException(userId);
    companyService.throwIfNotOwnerOrAdmin(
        company, AuthenticationUtil.getAuthentication().getAuth());

    List<Campaign> campaignList = campaignService.findAllByCompany(company);

    return findAllPageableByCampaigns(campaignList, pageable).map(CampaignDocumentResponseDto::new);
  }

  @Override
  public List<CampaignDocumentResponseDto> getCampaignDocuments(String campaignName) {
    Campaign campaign = campaignService.findByUrlFriendlyNameOrThrowException(campaignName);

    return findAllByCampaignOrderByUploadDateDesc(campaign).stream()
        .filter(this::hasReadAccess)
        .map(CampaignDocumentResponseDto::new)
        .collect(Collectors.toList());
  }

  private CampaignDocument convertDocumentDtoToDocument(
      CampaignDocumentDto campaignDocumentDto, Campaign campaign) {
    Optional<DocumentAccessLevel> accessLevel =
        this.documentAccessLevelRepository.findByName(campaignDocumentDto.getAccessLevel());
    Optional<DocumentType> type =
        this.documentTypeRepository.findByName(campaignDocumentDto.getType());
    if (!accessLevel.isPresent() || !type.isPresent()) {
      throw new BadRequestException(ExceptionMessages.INVALID_REQUEST);
    }

    return CampaignDocument.campaignDocumentBuilder()
        .title(campaignDocumentDto.getTitle())
        .accessLevel(accessLevel.get())
        .type(type.get())
        .url(campaignDocumentDto.getUrl())
        .campaign(campaign)
        .build();
  }
}
