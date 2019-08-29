package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.PaymentConfirmationDto;
import io.realmarket.propeler.api.dto.PaymentResponseDto;
import io.realmarket.propeler.model.BankTransferPayment;
import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.PayPalPayment;
import io.realmarket.propeler.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentService {

  List<String> getPaymentMethods(Long investmentId);

  BankTransferPayment getBankTransferPayment(Long investmentId);

  BankTransferPayment createBankTransferPayment(Investment investment);

  String getProformaInvoiceUrl(Long investmentId);

  Page<PaymentResponseDto> getPayments(Pageable pageable, String filter);

  Payment confirmBankTransferPayment(
      Long investmentId, PaymentConfirmationDto paymentConfirmationDto);

  PayPalPayment confirmPayPalPayment(String payPalOrderId, Long investmentId);

  String getInvoice(Long investmentId);
}
