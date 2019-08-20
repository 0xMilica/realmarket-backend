package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.CardPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardPaymentRepository extends JpaRepository<CardPayment, Long> {

  Optional<CardPayment> findByInvestmentId(Long investmentId);
}
