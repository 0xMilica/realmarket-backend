package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.PayPalPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayPalPaymentRepository extends JpaRepository<PayPalPayment, Long> {

  Optional<PayPalPayment> findByInvestmentId(Long investmentId);
}
