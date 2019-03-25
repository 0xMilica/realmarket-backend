package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.repository.CompanyRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.CompanyService;
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

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.COMPANY_ALREADY_EXIST;
import static io.realmarket.propeler.service.exception.util.ExceptionMessages.USER_IS_NOT_OWNER_OF_COMPANY;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

  private final CompanyRepository companyRepository;
  private final CloudObjectStorageService cloudObjectStorageService;
  private final ModelMapperBlankString modelMapperBlankString;

  @Value(value = "${cos.file_prefix.company_logo}")
  private String companyLogoPrefix;

  @Value(value = "${cos.file_prefix.company_featured_image}")
  private String companyFeaturedImage;

  @Autowired
  public CompanyServiceImpl(
      CompanyRepository companyRepository,
      CloudObjectStorageService cloudObjectStorageService,
      ModelMapperBlankString modelMapperBlankString) {
    this.companyRepository = companyRepository;
    this.cloudObjectStorageService = cloudObjectStorageService;
    this.modelMapperBlankString = modelMapperBlankString;
  }

  private static void throwIfNoAccess(Company company) {
    if (!AuthenticationUtil.getAuthentication()
        .getAuth()
        .getId()
        .equals(company.getAuth().getId())) {
      throw new ForbiddenOperationException(USER_IS_NOT_OWNER_OF_COMPANY);
    }
  }

  public Company save(Company company) {
    if (companyRepository.existsCompanyByAuth(AuthenticationUtil.getAuthentication().getAuth())) {
      throw new ForbiddenOperationException(COMPANY_ALREADY_EXIST);
    }
    return companyRepository.save(company);
  }

  public Company patch(Long companyId, Company companyPatch) {
    Company company = findByIdOrThrowException(companyId);
    throwIfNoAccess(company);
    modelMapperBlankString.map(companyPatch, company);
    return companyRepository.save(company);
  }

  public Company findByIdOrThrowException(Long id) {
    return companyRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Company with provided id does not exist."));
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
}
