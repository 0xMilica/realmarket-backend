package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.CompanyDocumentDto;
import io.realmarket.propeler.api.dto.CompanyDocumentResponseDto;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.CompanyDocument;

import java.util.List;

public interface CompanyDocumentService {

  List<CompanyDocument> findAllByCompany(Company company);

  List<CompanyDocument> findAllByCompanyOrderByUploadDateDesc(Company company);

  CompanyDocument findByIdOrThrowException(Long documentId);

  CompanyDocument submitDocument(CompanyDocumentDto companyDocumentDto, Long companyId);

  CompanyDocument patchCompanyDocument(Long documentId, CompanyDocumentDto companyDocumentDto);

  void deleteDocument(Long documentId);

  List<CompanyDocumentResponseDto> getUserCompanyDocuments(Long userId);

  List<CompanyDocumentResponseDto> getCompanyDocuments(Long companyId);
}
