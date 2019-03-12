package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.repository.CompanyRepository;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.CompanyService;
import io.realmarket.propeler.service.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

  private final CompanyRepository companyRepository;
  private final CloudObjectStorageService cloudObjectStorageService;

  @Value(value = "${cos.file_prefix.company_logo}")
  private String companyLogoPrefix;

  @Value(value = "${cos.file_prefix.company_featured_image}")
  private String companyFeaturedImage;

  @Autowired
  public CompanyServiceImpl(
      CompanyRepository companyRepository, CloudObjectStorageService cloudObjectStorageService) {
    this.companyRepository = companyRepository;
    this.cloudObjectStorageService = cloudObjectStorageService;
  }

  public Company save(Company company) {
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
    String url = companyLogoPrefix + company.getId() + "." + extension;
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
    log.info("Delete company[{}] logo requested",companyId);
    Company company = findByIdOrThrowException(companyId);
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
    String url = companyFeaturedImage + company.getId() + "." + extension;
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
    log.info("Delete company[{}] featured image requested",companyId);
    Company company = findByIdOrThrowException(companyId);
    cloudObjectStorageService.delete(company.getFeaturedImageUrl());
    company.setFeaturedImageUrl(null);
    companyRepository.save(company);
  }
}
