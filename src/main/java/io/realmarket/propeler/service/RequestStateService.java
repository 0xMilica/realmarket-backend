package io.realmarket.propeler.service;

import io.realmarket.propeler.model.RequestState;
import io.realmarket.propeler.model.enums.RequestStateName;

public interface RequestStateService {

  RequestState getRequestState(String name);

  RequestState getRequestState(RequestStateName requestStateName);
}
