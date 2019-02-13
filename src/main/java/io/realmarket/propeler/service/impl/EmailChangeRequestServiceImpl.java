package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.EmailChangeRequest;
import io.realmarket.propeler.repository.EmailChangeRequestRepository;
import io.realmarket.propeler.service.EmailChangeRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailChangeRequestServiceImpl implements EmailChangeRequestService {

  private EmailChangeRequestRepository emailChangeRequestRepository;

  @Autowired
  public EmailChangeRequestServiceImpl(EmailChangeRequestRepository emailChangeRequestRepository) {
    this.emailChangeRequestRepository = emailChangeRequestRepository;
  }

  public void save(EmailChangeRequest emailChangeRequest) {
    emailChangeRequestRepository.save(emailChangeRequest);
  }
}
