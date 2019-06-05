package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.DocumentResponseDto;
import io.realmarket.propeler.model.Campaign;
import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CampaignDocumentService;
import io.realmarket.propeler.service.CampaignService;
import io.realmarket.propeler.service.CompanyService;
import io.realmarket.propeler.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DocumentServiceImpl implements DocumentService {

  private final CompanyService companyService;
  private final CampaignService campaignService;
  private final CampaignDocumentService campaignDocumentService;

  @Autowired
  public DocumentServiceImpl(
      CompanyService companyService,
      CampaignService campaignService,
      CampaignDocumentService campaignDocumentService) {
    this.companyService = companyService;
    this.campaignService = campaignService;
    this.campaignDocumentService = campaignDocumentService;
  }

  @Override
  public List<DocumentResponseDto> getDocuments(Long userId) {
    Company company = companyService.findByAuthIdOrThrowException(userId);
    companyService.throwIfNotOwnerOrAdmin(
        company, AuthenticationUtil.getAuthentication().getAuth());

    List<DocumentResponseDto> documentList = new ArrayList<>();
    List<Campaign> campaignList = campaignService.findAllByCompany(company);
    campaignList.stream()
        .flatMap(campaign -> campaignDocumentService.findAllByCampaign(campaign).stream())
        .forEach(campaignDocument -> documentList.add(new DocumentResponseDto(campaignDocument)));

    return documentList;
  }
}
