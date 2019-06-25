package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.CompanyPatchDto;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Company;
import org.springframework.web.multipart.MultipartFile;

public interface CompanyService {

  Company save(Company company);

  Company patch(Long companyId, CompanyPatchDto companyPatch);

  Company findByIdOrThrowException(Long id);

  Company findByAuthOrThrowException(Auth owner);

  Company findMyCompany();

  void throwIfNoAccess(Company company);

  void throwIfNotOwnerOrAdmin(Company company, Auth auth);

  void uploadLogo(Long companyId, MultipartFile logo);

  FileDto downloadLogo(Long companyId);

  void deleteLogo(Long companyId);

  void uploadFeaturedImage(Long companyId, MultipartFile featuredImage);

  FileDto downloadFeaturedImage(Long companyId);

  void deleteFeaturedImage(Long companyId);

  Company findByAuthIdOrThrowException(final Long authId);

  void throwIfNotCompanyOwner();

  boolean isCompanyOwner(Auth auth);

  boolean isOwner(Company company);
}
