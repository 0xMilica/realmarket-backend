package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.InvestmentController;
import io.realmarket.propeler.service.InvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/investments")
public class InvestmentControllerImpl implements InvestmentController {

  private final InvestmentService investmentService;

  @Autowired
  public InvestmentControllerImpl(InvestmentService investmentService) {
    this.investmentService = investmentService;
  }

  @DeleteMapping("/{investmentId}")
  @PreAuthorize("hasAuthority('ROLE_INVESTOR')")
  public ResponseEntity<Void> revokeInvestment(@PathVariable Long investmentId) {
    investmentService.revokeInvestment(investmentId);
    return ResponseEntity.noContent().build();
  }
}
