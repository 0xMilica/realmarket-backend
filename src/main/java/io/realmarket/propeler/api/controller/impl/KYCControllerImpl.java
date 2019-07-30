package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.KYCController;
import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.service.UserKYCService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/kyc")
public class KYCControllerImpl implements KYCController {

  private final UserKYCService userKYCService;

  public KYCControllerImpl(UserKYCService userKYCService) {
    this.userKYCService = userKYCService;
  }

  @Override
  @PostMapping("/user")
  @PreAuthorize("hasAnyAuthority('ROLE_ENTREPRENEUR', 'ROLE_INVESTOR')")
  public ResponseEntity<UserKYCDto> requestUserKYC() {
    return ResponseEntity.ok(new UserKYCDto(userKYCService.createUserKYCRequest()));
  }

  @Override
  @PatchMapping("/user/assign")
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AUDITOR')")
  public ResponseEntity<UserKYCDto> assignUserKYC(
      @RequestBody UserKYCAssignmentDto userKYCAssignmentDto) {
    return ResponseEntity.ok(new UserKYCDto(userKYCService.assignUserKYC(userKYCAssignmentDto)));
  }

  @Override
  @GetMapping("/user/{userKYCId}")
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AUDITOR')")
  public ResponseEntity<UserKYCResponseWithFilesDto> getUserKYC(@PathVariable Long userKYCId) {
    return ResponseEntity.ok(userKYCService.getUserKYC(userKYCId));
  }

  @Override
  @GetMapping("/users")
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AUDITOR')")
  public ResponseEntity<Page<UserKYCResponseDto>> getUserKYCs(
      Pageable pageable,
      @RequestParam(value = "state", required = false, defaultValue = "all") String state,
      @RequestParam(value = "role", required = false, defaultValue = "investor") String role) {
    return ResponseEntity.ok(userKYCService.getUserKYCs(pageable, state, role));
  }

  @Override
  @PatchMapping("/user/{userKYCId}/accept")
  public ResponseEntity<UserKYCDto> approveUserKYC(@PathVariable Long userKYCId) {
    return ResponseEntity.ok(new UserKYCDto(userKYCService.approveUserKYC(userKYCId)));
  }

  @Override
  @PatchMapping("/user/{userKYCId}/reject")
  public ResponseEntity<UserKYCDto> rejectUserKYC(
      @PathVariable Long userKYCId, @RequestBody AuditDeclineDto userKYCRejectDto) {
    return ResponseEntity.ok(
        new UserKYCDto(userKYCService.rejectUserKYC(userKYCId, userKYCRejectDto.getContent())));
  }
}
