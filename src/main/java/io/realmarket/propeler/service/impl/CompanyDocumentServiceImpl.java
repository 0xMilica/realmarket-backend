package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CompanyDocumentDto;
import io.realmarket.propeler.api.dto.CompanyDocumentResponseDto;
import io.realmarket.propeler.model.*;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.CompanyDocumentRepository;
import io.realmarket.propeler.repository.DocumentAccessLevelRepository;
import io.realmarket.propeler.repository.DocumentRepository;
import io.realmarket.propeler.repository.DocumentTypeRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.CompanyDocumentService;
import io.realmarket.propeler.service.CompanyService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class CompanyDocumentServiceImpl implements CompanyDocumentService {

  private final CompanyDocumentRepository companyDocumentRepository;
  private final DocumentRepository documentRepository;
  private final DocumentAccessLevelRepository documentAccessLevelRepository;
  private final DocumentTypeRepository documentTypeRepository;
  private final CompanyService companyService;
  private final CloudObjectStorageService cloudObjectStorageService;
  private final ModelMapperBlankString modelMapperBlankString;

  @Autowired
  public CompanyDocumentServiceImpl(
      CompanyDocumentRepository companyDocumentRepository,
      DocumentRepository documentRepository,
      DocumentAccessLevelRepository documentAccessLevelRepository,
      DocumentTypeRepository documentTypeRepository,
      CompanyService companyService,
      CloudObjectStorageService cloudObjectStorageService,
      ModelMapperBlankString modelMapperBlankString) {
    this.companyDocumentRepository = companyDocumentRepository;
    this.documentRepository = documentRepository;
    this.documentAccessLevelRepository = documentAccessLevelRepository;
    this.documentTypeRepository = documentTypeRepository;
    this.companyService = companyService;
    this.cloudObjectStorageService = cloudObjectStorageService;
    this.modelMapperBlankString = modelMapperBlankString;
  }

  @Override
  public List<CompanyDocument> findAllByCompany(Company company) {
    return companyDocumentRepository.findAllByCompany(company);
  }

  @Override
  public CompanyDocument findByIdOrThrowException(Long documentId) {
    return companyDocumentRepository.findById(documentId).orElseThrow(EntityNotFoundException::new);
  }

  @Override
  public List<CompanyDocument> findAllByCompanyOrderByUploadDateDesc(Company company) {
    return companyDocumentRepository.findAllByCompanyOrderByUploadDateDesc(company);
  }

  @Override
  @Transactional
  public CompanyDocument submitDocument(CompanyDocumentDto companyDocumentDto, Long companyId) {
    Company company = companyService.findByIdOrThrowException(companyId);
    companyService.throwIfNoAccess(company);

    CompanyDocument companyDocument = convertDocumentDtoToDocument(companyDocumentDto, company);
    companyDocument.setUploadDate(Instant.now());

    if (!cloudObjectStorageService.doesFileExist(companyDocument.getUrl())) {
      throw new EntityNotFoundException(ExceptionMessages.FILE_DOES_NOT_EXIST);
    }

    return companyDocumentRepository.save(companyDocument);
  }

  @Override
  @Transactional
  public CompanyDocument patchCompanyDocument(
      Long documentId, CompanyDocumentDto companyDocumentDto) {
    CompanyDocument companyDocument = findByIdOrThrowException(documentId);
    companyService.throwIfNoAccess(companyDocument.getCompany());

    CompanyDocument companyDocumentPatch =
        convertDocumentDtoToDocument(companyDocumentDto, companyDocument.getCompany());

    if (!cloudObjectStorageService.doesFileExist(companyDocumentPatch.getUrl())) {
      throw new EntityNotFoundException(ExceptionMessages.FILE_DOES_NOT_EXIST);
    }

    modelMapperBlankString.map(companyDocumentPatch, companyDocument);
    return companyDocumentRepository.save(companyDocument);
  }

  @Override
  public void deleteDocument(Long documentId) {
    CompanyDocument companyDocument = findByIdOrThrowException(documentId);

    companyService.throwIfNoAccess(companyDocument.getCompany());
    cloudObjectStorageService.delete(companyDocument.getUrl());
    companyDocumentRepository.delete(companyDocument);
  }

  @Override
  public List<CompanyDocumentResponseDto> getUserCompanyDocuments(Long userId) {
    Company company = companyService.findByAuthIdOrThrowException(userId);
    companyService.throwIfNotOwnerOrAdmin(
        company, AuthenticationUtil.getAuthentication().getAuth());

    return findAllByCompany(company).stream()
        .map(CompanyDocumentResponseDto::new)
        .collect(Collectors.toList());
  }

  @Override
  public List<CompanyDocumentResponseDto> getCompanyDocuments(Long companyId) {
    Company company = companyService.findByIdOrThrowException(companyId);

    return findAllByCompanyOrderByUploadDateDesc(company).stream()
        .filter(this::hasReadAccess)
        .map(CompanyDocumentResponseDto::new)
        .collect(Collectors.toList());
  }

  public boolean hasReadAccess(CompanyDocument companyDocument) {
    Auth auth = AuthenticationUtil.getAuthOrReturnNull();
    DocumentAccessLevel accessLevel = companyDocument.getAccessLevel();
    UserRoleName userRoleName = (auth == null) ? null : auth.getUserRole().getName();
    if (auth != null && companyService.isOwner(companyDocument.getCompany())) {
      return true;
    }
    return DocumentAccessLevel.hasReadAccess(accessLevel, userRoleName, true);
  }

  private CompanyDocument convertDocumentDtoToDocument(
      CompanyDocumentDto companyDocumentDto, Company company) {
    Optional<DocumentAccessLevel> accessLevel =
        this.documentAccessLevelRepository.findByName(companyDocumentDto.getAccessLevel());
    Optional<DocumentType> type =
        this.documentTypeRepository.findByName(companyDocumentDto.getType());
    if (!accessLevel.isPresent() || !type.isPresent()) {
      throw new BadRequestException(ExceptionMessages.INVALID_REQUEST);
    }

    return CompanyDocument.companyDocumentBuilder()
        .title(companyDocumentDto.getTitle())
        .accessLevel(accessLevel.get())
        .type(type.get())
        .url(companyDocumentDto.getUrl())
        .company(company)
        .build();
  }
}
