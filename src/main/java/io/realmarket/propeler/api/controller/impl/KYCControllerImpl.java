package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.KYCController;
import io.realmarket.propeler.api.dto.UserKYCDto;
import io.realmarket.propeler.service.UserKYCService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
