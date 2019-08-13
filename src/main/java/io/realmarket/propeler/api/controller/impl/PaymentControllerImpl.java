package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.PaymentController;
import io.realmarket.propeler.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentControllerImpl implements PaymentController {

  private final PaymentService paymentService;

  @Autowired
  public PaymentControllerImpl(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @Override
  @GetMapping
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity getPayments(
      Pageable pageable, @RequestParam(value = "filter", required = false) String filter) {
    return ResponseEntity.ok(paymentService.getPayments(pageable, filter));
  }
}
