package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Document;
import io.realmarket.propeler.model.Payment;

public interface PaymentDocumentService {
  Document submitDocument(Payment payment, String documentUrl, String documentTitle);
}
