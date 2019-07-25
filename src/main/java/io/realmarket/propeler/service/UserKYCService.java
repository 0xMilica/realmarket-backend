package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.UserKYCAssignmentDto;
import io.realmarket.propeler.api.dto.UserKYCResponseDto;
import io.realmarket.propeler.api.dto.UserKYCResponseWithFilesDto;
import io.realmarket.propeler.model.UserKYC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserKYCService {

  UserKYC createUserKYCRequest();

  UserKYC assignUserKYC(UserKYCAssignmentDto userKYCAssignmentDto);

  UserKYCResponseWithFilesDto getUserKYC(Long userKYCId);

  Page<UserKYCResponseDto> getUserKYCs(Pageable pageable);
}
