package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.BankTransferPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankTransferPaymentRepository extends JpaRepository<BankTransferPayment, Long> {

  Optional<BankTransferPayment> findByInvestmentId(Long investmentId);
}
