package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.RequestState;
import io.realmarket.propeler.model.enums.RequestStateName;
import io.realmarket.propeler.repository.RequestStateRepository;
import io.realmarket.propeler.service.RequestStateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.REQUEST_STATE_NOT_FOUND;

@Service
@Slf4j
public class RequestStateServiceImpl implements RequestStateService {

  private final RequestStateRepository requestStateRepository;

  @Autowired
  public RequestStateServiceImpl(RequestStateRepository requestStateRepository) {
    this.requestStateRepository = requestStateRepository;
  }

  @Override
  public RequestState getRequestState(String name) {
    return requestStateRepository
        .findByName(RequestStateName.valueOf(name.toUpperCase()))
        .orElseThrow(() -> new EntityNotFoundException(REQUEST_STATE_NOT_FOUND));
  }

  @Override
  public RequestState getRequestState(RequestStateName requestStateName) {
    return requestStateRepository
        .findByName(requestStateName)
        .orElseThrow(() -> new EntityNotFoundException(REQUEST_STATE_NOT_FOUND));
  }
}
