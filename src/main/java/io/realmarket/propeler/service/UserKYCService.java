package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.UserKYCAssignmentDto;
import io.realmarket.propeler.api.dto.UserKYCRequestDto;
import io.realmarket.propeler.api.dto.UserKYCResponseDto;
import io.realmarket.propeler.api.dto.UserKYCResponseWithFilesDto;
import io.realmarket.propeler.model.UserKYC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserKYCService {

  UserKYC submitUserKYCRequest(UserKYCRequestDto userKYCRequestDto);

  UserKYC assignUserKYC(UserKYCAssignmentDto userKYCAssignmentDto);

  UserKYCResponseWithFilesDto getUserKYC();

  UserKYCResponseWithFilesDto getUserKYC(Long userKYCId);

  Page<UserKYCResponseDto> getUserKYCs(Pageable pageable, String requestState, String role);

  UserKYC approveUserKYC(Long userKYCId);

  UserKYC rejectUserKYC(Long userKYCId, String rejectionReason);
}
