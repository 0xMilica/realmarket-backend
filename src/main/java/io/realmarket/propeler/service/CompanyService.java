package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.model.Company;
import org.springframework.web.multipart.MultipartFile;

public interface CompanyService {

  Company save(Company company);

  Company findByIdOrThrowException(Long id);

  void uploadLogo(Long companyId, MultipartFile logo);

  FileDto downloadLogo(Long companyId);

  void deleteLogo(Long companyId);
}
