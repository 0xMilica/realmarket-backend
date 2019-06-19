package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.InvestmentController;
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

  @DeleteMapping("/{investID}")
  @PreAuthorize("hasAuthority('ROLE_INVESTOR')")
  public ResponseEntity<Void> revokeInvestment(@PathVariable Long investID) {
    investmentService.cancelInvestment(investID);
    return ResponseEntity.noContent().build();
  }
}
