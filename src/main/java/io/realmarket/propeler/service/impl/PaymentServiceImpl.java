package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.PaymentConfirmationDto;
import io.realmarket.propeler.api.dto.PaymentResponseDto;
import io.realmarket.propeler.model.BankTransferPayment;
import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.Payment;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.model.enums.InvestmentStateName;
import io.realmarket.propeler.repository.BankTransferPaymentRepository;
import io.realmarket.propeler.repository.InvestmentRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.InvestmentStateService;
import io.realmarket.propeler.service.PaymentDocumentService;
import io.realmarket.propeler.service.PaymentService;
import io.realmarket.propeler.service.blockchain.BlockchainCommunicationService;
import io.realmarket.propeler.service.blockchain.BlockchainMethod;
import io.realmarket.propeler.service.blockchain.dto.investment.payment.PaymentDto;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.INVALID_REQUEST;

@Service
public class PaymentServiceImpl implements PaymentService {

  private final PaymentDocumentService paymentDocumentService;
  private final InvestmentStateService investmentStateService;
  private final InvestmentRepository investmentRepository;
  private final BankTransferPaymentRepository bankTransferPaymentRepository;
  private final BlockchainCommunicationService blockchainCommunicationService;

  @Autowired
  public PaymentServiceImpl(
      PaymentDocumentService paymentDocumentService,
      InvestmentStateService investmentStateService,
      InvestmentRepository investmentRepository,
      BankTransferPaymentRepository bankTransferPaymentRepository,
      BlockchainCommunicationService blockchainCommunicationService) {
    this.paymentDocumentService = paymentDocumentService;
    this.investmentStateService = investmentStateService;
    this.investmentRepository = investmentRepository;
    this.bankTransferPaymentRepository = bankTransferPaymentRepository;
    this.blockchainCommunicationService = blockchainCommunicationService;
  }

  @Override
  public Page<PaymentResponseDto> getPayments(Pageable pageable, String filter) {
    throwIfNotAllowedFilter(filter);
    InvestmentStateName state =
        (filter == null) ? null : InvestmentStateName.valueOf(filter.toUpperCase());
    return investmentRepository
        .findAllPaymentInvestment(state, pageable)
        .map(PaymentResponseDto::new);
  }

  @Transactional
  @Override
  public Payment confirmBankTransferPayment(
      Long investmentId, PaymentConfirmationDto paymentConfirmationDto) {
    Investment investment = investmentRepository.getOne(investmentId);

    if (!investment.getInvestmentState().getName().equals(InvestmentStateName.OWNER_APPROVED)) {
      throw new BadRequestException(INVALID_REQUEST);
    }

    BankTransferPayment bankTransferPayment = findBankTransferForInvestment(investmentId);

    paymentDocumentService.submitDocument(
        bankTransferPayment,
        paymentConfirmationDto.getDocumentUrl(),
        paymentConfirmationDto.getDocumentTitle());

    // TODO: Change later to date from bank transfer
    bankTransferPayment.setPaymentDate(Instant.now());

    investment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.PAID));
    investment.setPaymentDate(bankTransferPayment.getPaymentDate());
    investmentRepository.save(investment);

    bankTransferPayment = bankTransferPaymentRepository.save(bankTransferPayment);

    blockchainCommunicationService.invoke(
        BlockchainMethod.PAYMENT_CONFIRMED,
        new PaymentDto(
            bankTransferPayment, AuthenticationUtil.getAuthentication().getAuth().getId()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());

    return bankTransferPayment;
  }

  private void throwIfNotAllowedFilter(String filter) {
    if (filter == null) {
      return;
    }
    if (!(filter.equalsIgnoreCase("owner_approved")
        || filter.equalsIgnoreCase("paid")
        || filter.equalsIgnoreCase("expired"))) {
      throw new BadRequestException(ExceptionMessages.INVALID_REQUEST);
    }
  }

  @Override
  public void reserveFunds(Person person, BigDecimal amountOfMoney) {}

  @Override
  public boolean proceedToPayment(Person person, BigDecimal amountOfMoney) {
    return true;
  }

  @Override
  public void withdrawFunds(Person person, BigDecimal amountOfMoney) {}

  private BankTransferPayment findBankTransferForInvestment(Long investmentId) {
    return bankTransferPaymentRepository
        .findByInvestmentId(investmentId)
        .orElseThrow(EntityNotFoundException::new);
  }
}
