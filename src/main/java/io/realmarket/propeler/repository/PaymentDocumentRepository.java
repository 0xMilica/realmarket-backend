package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.PaymentDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDocumentRepository extends JpaRepository<PaymentDocument, Long> {}
