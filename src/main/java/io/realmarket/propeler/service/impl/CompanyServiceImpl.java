package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.CompanyPatchDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.CompanyEditRequest;
import io.realmarket.propeler.model.enums.UserRoleName;
import io.realmarket.propeler.repository.CompanyRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.AdministratorService;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.CompanyService;
import io.realmarket.propeler.service.blockchain.BlockchainMethod;
import io.realmarket.propeler.service.blockchain.dto.company.CompanyEditRequestDto;
import io.realmarket.propeler.service.blockchain.dto.company.CompanyRegistrationDto;
import io.realmarket.propeler.service.blockchain.queue.BlockchainMessageProducer;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.ForbiddenOperationException;
import io.realmarket.propeler.service.util.FileUtils;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.*;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

  private final CompanyRepository companyRepository;
  private final AdministratorService administratorService;
  private final CloudObjectStorageService cloudObjectStorageService;
  private final ModelMapperBlankString modelMapperBlankString;
  private final BlockchainMessageProducer blockchainMessageProducer;

  @Value(value = "${cos.file_prefix.company_logo}")
  private String companyLogoPrefix;

  @Value(value = "${cos.file_prefix.company_featured_image}")
  private String companyFeaturedImage;

  @Autowired
  public CompanyServiceImpl(
      CompanyRepository companyRepository,
      AdministratorService administratorService,
      CloudObjectStorageService cloudObjectStorageService,
      ModelMapperBlankString modelMapperBlankString,
      BlockchainMessageProducer blockchainMessageProducer) {
    this.companyRepository = companyRepository;
    this.administratorService = administratorService;
    this.cloudObjectStorageService = cloudObjectStorageService;
    this.modelMapperBlankString = modelMapperBlankString;
    this.blockchainMessageProducer = blockchainMessageProducer;
  }

  public void throwIfNoAccess(Company company) {
    if (!AuthenticationUtil.getAuthentication()
        .getAuth()
        .getId()
        .equals(company.getAuth().getId())) {
      throw new ForbiddenOperationException(NOT_COMPANY_OWNER);
    }
  }

  public Company save(Company company) {
    if (companyRepository.existsCompanyByAuth(AuthenticationUtil.getAuthentication().getAuth())) {
      throw new ForbiddenOperationException(COMPANY_ALREADY_EXISTS);
    }
    company = companyRepository.save(company);

    blockchainMessageProducer.produceMessage(
        BlockchainMethod.COMPANY_REGISTRATION,
        new CompanyRegistrationDto(company),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());

    return company;
  }

  public Company patch(Long companyId, CompanyPatchDto companyPatchDto) {
    Company company = findByIdOrThrowException(companyId);
    throwIfNoAccess(company);
    if (companyPatchDto.shouldAdminBeCalled()) {
      CompanyEditRequest editRequest = companyPatchDto.buildCompanyEditRequest(company);
      administratorService.requestCompanyEdit(editRequest);

      blockchainMessageProducer.produceMessage(
          BlockchainMethod.COMPANY_EDIT_REQUEST,
          new CompanyEditRequestDto(editRequest),
          AuthenticationUtil.getAuthentication().getAuth().getUsername(),
          AuthenticationUtil.getClientIp());

      return company;
    }
    modelMapperBlankString.map(companyPatchDto, company);
    return companyRepository.save(company);
  }

  public Company findByIdOrThrowException(Long id) {
    return companyRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException(COMPANY_DOES_NOT_EXIST));
  }

  @Override
  public Company findByAuthOrThrowException(Auth owner) {
    return companyRepository
        .findByAuth(owner)
        .orElseThrow(() -> new EntityNotFoundException(ENTREPRENEUR_MISSING_COMPANY));
  }

  @Override
  @Transactional
  public void uploadLogo(Long companyId, MultipartFile logo) {
    log.info("Logo upload requested");
    String extension = FileUtils.getExtensionOrThrowException(logo);
    Company company = findByIdOrThrowException(companyId);
    throwIfNoAccess(company);
    String url = String.join("", companyLogoPrefix, company.getId().toString(), ".", extension);
    cloudObjectStorageService.uploadAndReplace(company.getLogoUrl(), url, logo);
    company.setLogoUrl(url);
    companyRepository.save(company);
  }

  @Override
  public Company findMyCompany() {
    return findByAuthOrThrowException(AuthenticationUtil.getAuthentication().getAuth());
  }

  @Override
  public void throwIfNotOwnerOrAdmin(Company company, Auth auth) {
    if (auth.getUserRole().getName().equals(UserRoleName.ROLE_ADMIN)) {
      return;
    } else if (auth.getUserRole().getName().equals(UserRoleName.ROLE_ENTREPRENEUR)) {
      if (!company.getAuth().getId().equals(auth.getId())) {
        throw new BadRequestException(NOT_COMPANY_OWNER);
      }
    }
  }

  @Override
  public FileDto downloadLogo(Long companyId) {
    return cloudObjectStorageService.downloadFileDto(
        findByIdOrThrowException(companyId).getLogoUrl());
  }

  @Override
  @Transactional
  public void deleteLogo(Long companyId) {
    log.info("Delete company[{}] logo requested", companyId);
    Company company = findByIdOrThrowException(companyId);
    throwIfNoAccess(company);
    cloudObjectStorageService.delete(company.getLogoUrl());

    company.setLogoUrl(null);
    companyRepository.save(company);
  }

  @Override
  @Transactional
  public void uploadFeaturedImage(Long companyId, MultipartFile logo) {
    log.info("Featured image upload requested");
    String extension = FileUtils.getExtensionOrThrowException(logo);
    Company company = findByIdOrThrowException(companyId);
    throwIfNoAccess(company);
    String url = String.join("", companyFeaturedImage, company.getId().toString(), ".", extension);
    cloudObjectStorageService.uploadAndReplace(company.getFeaturedImageUrl(), url, logo);
    company.setFeaturedImageUrl(url);
    companyRepository.save(company);
  }

  @Override
  public FileDto downloadFeaturedImage(Long companyId) {
    return cloudObjectStorageService.downloadFileDto(
        findByIdOrThrowException(companyId).getFeaturedImageUrl());
  }

  @Override
  @Transactional
  public void deleteFeaturedImage(Long companyId) {
    log.info("Delete company[{}] featured image requested", companyId);
    Company company = findByIdOrThrowException(companyId);
    throwIfNoAccess(company);
    cloudObjectStorageService.delete(company.getFeaturedImageUrl());
    company.setFeaturedImageUrl(null);
    companyRepository.save(company);
  }

  @Override
  public Company findByAuthIdOrThrowException(final Long authId) {
    return companyRepository
        .findByAuthId(authId)
        .orElseThrow(() -> new EntityNotFoundException(NOT_COMPANY_OWNER));
  }

  public void throwIfNotCompanyOwner() {
    Auth owner = AuthenticationUtil.getAuthentication().getAuth();
    if (!isCompanyOwner(owner)) {
      throw new ForbiddenOperationException(NOT_COMPANY_OWNER);
    }
  }

  public boolean isCompanyOwner(Auth auth) {
    return companyRepository.existsCompanyByAuth(auth);
  }

  @Override
  public boolean isOwner(Company company) {
    return company
        .getAuth()
        .getId()
        .equals(AuthenticationUtil.getAuthentication().getAuth().getId());
  }
}
