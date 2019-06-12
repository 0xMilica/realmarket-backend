package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.CompanyEditRequest;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.CompanyEditRequestRepository;
import io.realmarket.propeler.service.CompanyEditRequestService;
import io.realmarket.propeler.service.RequestStateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CompanyEditRequestServiceImpl implements CompanyEditRequestService {
  private final CompanyEditRequestRepository companyEditRequestRepository;
  private final RequestStateService requestStateService;

  @Autowired
  public CompanyEditRequestServiceImpl(
      CompanyEditRequestRepository companyEditRequestRepository,
      RequestStateService requestStateService) {
    this.companyEditRequestRepository = companyEditRequestRepository;
    this.requestStateService = requestStateService;
  }

  @Override
  public CompanyEditRequest createCompanyEditRequest(CompanyEditRequest companyEditRequest) {
    companyEditRequest.setRequestState(
        requestStateService.getRequestState(RequestStateName.PENDING));

    return companyEditRequestRepository.save(companyEditRequest);
  }
}
