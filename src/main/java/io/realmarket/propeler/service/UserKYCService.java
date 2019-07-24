package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.UserKYCAssignmentDto;
import io.realmarket.propeler.model.UserKYC;

public interface UserKYCService {

  UserKYC createUserKYCRequest();

  UserKYC assignUserKYC(UserKYCAssignmentDto userKYCAssignmentDto);
}
