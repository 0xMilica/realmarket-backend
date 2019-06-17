package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.CompanyEditRequest;
import io.realmarket.propeler.service.AdministratorService;
import io.realmarket.propeler.service.CompanyEditRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdministratorServiceImpl implements AdministratorService {
  private final CompanyEditRequestService companyEditRequestService;

  @Autowired
  public AdministratorServiceImpl(CompanyEditRequestService companyEditRequestService) {
    this.companyEditRequestService = companyEditRequestService;
  }

  @Override
  public Long requestCompanyEdit(CompanyEditRequest companyEditRequest) {
    companyEditRequest = companyEditRequestService.createCompanyEditRequest(companyEditRequest);
    if (companyEditRequest != null) return companyEditRequest.getId();
    return -1L;
  }
}
