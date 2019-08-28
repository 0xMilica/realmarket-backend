package io.realmarket.propeler.service.impl;

import com.paypal.orders.Order;
import io.realmarket.propeler.api.dto.AttachmentFileDto;
import io.realmarket.propeler.api.dto.PaymentConfirmationDto;
import io.realmarket.propeler.api.dto.PaymentResponseDto;
import io.realmarket.propeler.api.dto.enums.EmailType;
import io.realmarket.propeler.model.BankTransferPayment;
import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.PayPalPayment;
import io.realmarket.propeler.model.Payment;
import io.realmarket.propeler.model.enums.FileType;
import io.realmarket.propeler.model.enums.InvestmentStateName;
import io.realmarket.propeler.repository.BankTransferPaymentRepository;
import io.realmarket.propeler.repository.InvestmentRepository;
import io.realmarket.propeler.repository.PayPalPaymentRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.blockchain.BlockchainMethod;
import io.realmarket.propeler.service.blockchain.dto.investment.payment.PaymentDto;
import io.realmarket.propeler.service.blockchain.queue.BlockchainMessageProducer;
import io.realmarket.propeler.service.exception.AmountsNotEqualException;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.payment.PayPalClient;
import io.realmarket.propeler.service.util.MailContentHolder;
import io.realmarket.propeler.service.util.PdfService;
import io.realmarket.propeler.service.util.TemplateDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.INVALID_REQUEST;

@Service
public class PaymentServiceImpl implements PaymentService {
  private final PaymentDocumentService paymentDocumentService;
  private final InvestmentService investmentService;
  private final InvestmentStateService investmentStateService;
  private final PlatformSettingsService platformSettingsService;
  private final PdfService pdfService;
  private final FileService fileService;
  private final EmailService emailService;
  private final TemplateDataUtil templateDataUtil;
  private final InvestmentRepository investmentRepository;
  private final BankTransferPaymentRepository bankTransferPaymentRepository;
  private final PayPalPaymentRepository payPalPaymentRepository;
  private final PayPalClient payPalClient;
  private final BlockchainMessageProducer blockchainMessageProducer;

  @Value("${app.bank.account-number}")
  private String accountNumber;

  @Value("${app.bank.mod}")
  private String mod;

  @Value("${app.payment.card-limit}")
  private BigDecimal cardPaymentLimit;

  @Autowired
  public PaymentServiceImpl(
      PaymentDocumentService paymentDocumentService,
      InvestmentService investmentService,
      InvestmentStateService investmentStateService,
      PlatformSettingsService platformSettingsService,
      PdfService pdfService,
      FileService fileService,
      EmailService emailService,
      TemplateDataUtil templateDataUtil,
      InvestmentRepository investmentRepository,
      BankTransferPaymentRepository bankTransferPaymentRepository,
      PayPalPaymentRepository payPalPaymentRepository,
      PayPalClient payPalClient,
      BlockchainMessageProducer blockchainMessageProducer) {
    this.paymentDocumentService = paymentDocumentService;
    this.investmentService = investmentService;
    this.investmentStateService = investmentStateService;
    this.platformSettingsService = platformSettingsService;
    this.pdfService = pdfService;
    this.fileService = fileService;
    this.emailService = emailService;
    this.templateDataUtil = templateDataUtil;
    this.investmentRepository = investmentRepository;
    this.bankTransferPaymentRepository = bankTransferPaymentRepository;
    this.payPalPaymentRepository = payPalPaymentRepository;
    this.payPalClient = payPalClient;
    this.blockchainMessageProducer = blockchainMessageProducer;
  }

  @Override
  public List<String> getPaymentMethods(Long investmentId) {
    Investment investment = investmentRepository.getOne(investmentId);

    List<String> methods = new ArrayList<>();
    methods.add("Bank transfer");
    if (investment.getInvestedAmount().compareTo(cardPaymentLimit) < 1) {
      methods.add("PayPal");
    }
    return methods;
  }

  @Transactional
  @Override
  public BankTransferPayment getBankTransferPayment(Long investmentId) {
    Investment investment = investmentRepository.getOne(investmentId);

    Optional<BankTransferPayment> bankTransferPayment =
        findBankTransferPaymentForInvestment(investmentId);
    return bankTransferPayment.orElseGet(() -> createBankTransferPayment(investment));
  }

  @Override
  public BankTransferPayment createBankTransferPayment(Investment investment) {
    BankTransferPayment bankTransferPayment =
        BankTransferPayment.bankTransferPaymentBuilder()
            .investment(investment)
            .amount(investment.getInvestedAmount())
            .currency(platformSettingsService.getPlatformCurrency().getCode())
            .creationDate(Instant.now())
            .accountNumber(accountNumber)
            .routingNumber(createRoutingNumber(investment))
            .proformaInvoiceUrl(createProformaInvoiceUrl(investment))
            .build();

    return bankTransferPaymentRepository.save(bankTransferPayment);
  }

  @Override
  public String getProformaInvoiceUrl(Long investmentId) {
    investmentRepository.getOne(investmentId);

    BankTransferPayment bankTransferPayment =
        findBankTransferPaymentForInvestmentOrThrow(investmentId);
    return bankTransferPayment.getProformaInvoiceUrl();
  }

  private String createRoutingNumber(Investment investment) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < investment.getId().toString().length(); i++) {
      if (investment.getId().toString().charAt(i) == '0') {
        sb.append("00");
      }
      sb.append(investment.getId().toString().charAt(i));
    }
    sb.append("0");
    String personId = investment.getPerson().getId().toString();
    sb.append(personId);
    return sb.toString();
  }

  private String createProformaInvoiceUrl(Investment investment) {
    Map<String, Object> documentsParameters;
    byte[] file;
    if (investment.getPerson().getAuth() == null) {
      documentsParameters = templateDataUtil.getOffPlatformInvoiceData(investment);
      file = pdfService.generatePdf(documentsParameters, FileType.OFFPLATFORM_INVOICE);
    } else {
      documentsParameters = templateDataUtil.getData(investment, "Bank Transfer", true);
      file = pdfService.generatePdf(documentsParameters, FileType.PROFORMA_INVOICE);
    }
    String url = fileService.uploadFile(file, "pdf");

    if (investment.getPerson().getEmail() != null) {
      sendProformaInvoiceEmail(investment, file);
    }
    return url;
  }

  private void sendProformaInvoiceEmail(Investment investment, byte[] file) {
    if (investment.getPerson().getEmail() == null) {
      return;
    }

    Map<String, Object> parameters = new HashMap<>();

    emailService.sendMailToUser(
        new MailContentHolder(
            Collections.singletonList(investment.getPerson().getEmail()),
            EmailType.PROFORMA_INVOICE,
            parameters,
            new AttachmentFileDto(file, "ProformaInvoice", ".pdf")));
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

    BankTransferPayment bankTransferPayment =
        findBankTransferPaymentForInvestmentOrThrow(investmentId);

    paymentDocumentService.submitDocument(
        bankTransferPayment,
        paymentConfirmationDto.getDocumentUrl(),
        paymentConfirmationDto.getDocumentTitle());

    bankTransferPayment.setPaymentDate(paymentConfirmationDto.getPaymentDate().toInstant());

    investment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.PAID));
    investment.setPaymentDate(bankTransferPayment.getPaymentDate());
    investmentService.save(investment);

    bankTransferPayment = bankTransferPaymentRepository.save(bankTransferPayment);

    blockchainMessageProducer.produceMessage(
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

  private BankTransferPayment findBankTransferPaymentForInvestmentOrThrow(Long investmentId) {
    return bankTransferPaymentRepository
        .findByInvestmentId(investmentId)
        .orElseThrow(EntityNotFoundException::new);
  }

  @Transactional
  @Override
  public PayPalPayment confirmPayPalPayment(String orderId, Long investmentId) {
    Investment investment = investmentService.findByIdOrThrowException(investmentId);
    throwIfInvestmentAlreadyPaid(investment);

    Order order = payPalClient.getOrder(orderId);
    BigDecimal payPalAmount =
        new BigDecimal(order.purchaseUnits().get(0).amountWithBreakdown().value());

    throwIfAmountsNotEqual(payPalAmount, investment.getInvestedAmount());

    payPalClient.captureRequest(orderId);

    investment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.PAID));
    investment.setPaymentDate(Instant.now());
    investmentService.save(investment);

    return createPayPalPayment(investment, payPalAmount, orderId, order.createTime());
  }

  private void throwIfInvestmentAlreadyPaid(Investment investment) {
    if (investment.getInvestmentState().getName().equals(InvestmentStateName.PAID)) {
      throw new BadRequestException(ExceptionMessages.PAYMENT_ALREADY_EXISTS);
    }
  }

  private void throwIfAmountsNotEqual(BigDecimal payPalAmount, BigDecimal investmentAmount) {
    if (investmentAmount.compareTo(payPalAmount) != 0)
      throw new AmountsNotEqualException(ExceptionMessages.AMOUNTS_NOT_EQUAL);
  }

  private PayPalPayment createPayPalPayment(
      Investment investment, BigDecimal payPalAmount, String orderId, String createTime) {
    PayPalPayment payPalPayment =
        PayPalPayment.payPalPaymentBuilder()
            .investment(investment)
            .currency(platformSettingsService.getPlatformCurrency().getCode())
            .amount(payPalAmount)
            .payPalOrderId(orderId)
            .creationDate(Instant.now())
            .paymentDate(Instant.parse(createTime))
            .build();

    return payPalPaymentRepository.save(payPalPayment);
  }

  private Optional<BankTransferPayment> findBankTransferPaymentForInvestment(Long investmentId) {
    return bankTransferPaymentRepository.findByInvestmentId(investmentId);
  }
}
