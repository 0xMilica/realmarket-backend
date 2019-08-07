package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.InvestmentController;
import io.realmarket.propeler.api.dto.InvestmentResponseDto;
import io.realmarket.propeler.api.dto.OffPlatformInvestmentRequestDto;
import io.realmarket.propeler.service.InvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/investments")
public class InvestmentControllerImpl implements InvestmentController {

  private final InvestmentService investmentService;

  @Autowired
  public InvestmentControllerImpl(InvestmentService investmentService) {
    this.investmentService = investmentService;
  }

  @DeleteMapping("/{investmentId}")
  @PreAuthorize("hasAnyAuthority('ROLE_INDIVIDUAL_INVESTOR', 'ROLE_COMPANY_INVESTOR')")
  public ResponseEntity<Void> revokeInvestment(@PathVariable Long investmentId) {
    investmentService.revokeInvestment(investmentId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PatchMapping("/{investmentId}/ownerApprove")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<Void> ownerApproveInvestment(@PathVariable Long investmentId) {
    investmentService.ownerApproveInvestment(investmentId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PatchMapping("/{investmentId}/auditorApprove")
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AUDITOR')")
  public ResponseEntity<Void> auditorApproveInvestment(@PathVariable Long investmentId) {
    investmentService.auditorApproveInvestment(investmentId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PatchMapping("/{investmentId}/ownerReject")
  @PreAuthorize("hasAuthority('ROLE_ENTREPRENEUR')")
  public ResponseEntity<Void> ownerRejectInvestment(@PathVariable Long investmentId) {
    investmentService.ownerRejectInvestment(investmentId);
    return ResponseEntity.ok().build();
  }

  @Override
  @PatchMapping("/{investmentId}/auditorReject")
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AUDITOR')")
  public ResponseEntity<Void> auditorRejectInvestment(@PathVariable Long investmentId) {
    investmentService.auditorRejectInvestment(investmentId);
    return ResponseEntity.ok().build();
  }

  @Override
  @PostMapping("/offPlatform/{campaignName}")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<InvestmentResponseDto> registerOffPlatformInvestment(
      @RequestBody OffPlatformInvestmentRequestDto offPlatformInvestmentRequestDto,
      @PathVariable String campaignName) {
    return ResponseEntity.ok(
        new InvestmentResponseDto(
            investmentService.offPlatformInvest(offPlatformInvestmentRequestDto, campaignName)));
  }
}
