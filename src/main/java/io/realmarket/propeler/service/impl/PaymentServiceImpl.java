package io.realmarket.propeler.service.impl;

import com.paypal.orders.Order;
import io.realmarket.propeler.api.dto.PaymentConfirmationDto;
import io.realmarket.propeler.api.dto.PaymentResponseDto;
import io.realmarket.propeler.api.dto.enums.EmailType;
import io.realmarket.propeler.model.*;
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
import io.realmarket.propeler.service.util.PdfService;
import io.realmarket.propeler.service.util.TemplateDataUtil;
import io.realmarket.propeler.service.util.email.Parameters;
import io.realmarket.propeler.service.util.email.message.EmailAttachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static io.realmarket.propeler.service.exception.util.ExceptionMessages.INVALID_REQUEST;
import static io.realmarket.propeler.service.exception.util.ExceptionMessages.INVESTMENT_NOT_PAID;
import static io.realmarket.propeler.service.util.TemplateDataUtil.BANK_TRANSFER_PAYMENT_TYPE;
import static io.realmarket.propeler.service.util.TemplateDataUtil.PAYPAL_PAYMENT_TYPE;

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

  @Value(value = "${app.locale.timezone}")
  private String timeZone;

  @Autowired
  public PaymentServiceImpl(
      PaymentDocumentService paymentDocumentService,
      @Lazy InvestmentService investmentService,
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
    methods.add(BANK_TRANSFER_PAYMENT_TYPE);
    if (investment.getInvestedAmount().compareTo(cardPaymentLimit) < 1) {
      methods.add(PAYPAL_PAYMENT_TYPE);
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
    Map<String, Object> documentsParameters = templateDataUtil.getProformaInvoiceData(investment);
    byte[] file = pdfService.generatePdf(documentsParameters, FileType.PROFORMA_INVOICE);

    String url = fileService.uploadFile(file, "pdf");

    sendProformaInvoiceEmail(investment, file);
    return url;
  }

  private void sendProformaInvoiceEmail(Investment investment, byte[] file) {
    if (investment.getPerson().getEmail() != null) {
      Map<String, Object> content = new HashMap<>();
      content.put(Parameters.CAMPAIGN, investment.getCampaign().getName());
      content.put(Parameters.FIRST_NAME, investment.getPerson().getFirstName());
      content.put(Parameters.LAST_NAME, investment.getPerson().getLastName());
      content.put(Parameters.INVESTMENT, investment);
      LocalDateTime creationDate =
          LocalDateTime.ofInstant(investment.getCreationDate(), ZoneId.of(timeZone));
      content.put(
          Parameters.PROFORMA_INVOICE_NUMBER,
          investment.getCurrency() + "/" + creationDate.getYear() + "-" + investment.getId());

      emailService.sendEmailToUser(
          EmailType.PROFORMA_INVOICE,
          Collections.singletonList(investment.getPerson().getEmail()),
          content,
          new EmailAttachment(file, "ProformaInvoice", ".pdf"));
    }
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
    investment.setInvoiceUrl(createInvoice(investment, BANK_TRANSFER_PAYMENT_TYPE));
    investmentRepository.save(investment);

    bankTransferPayment = bankTransferPaymentRepository.save(bankTransferPayment);

    blockchainMessageProducer.produceMessage(
        BlockchainMethod.PAYMENT_CONFIRMED,
        new PaymentDto(
            bankTransferPayment, AuthenticationUtil.getAuthentication().getAuth().getId()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());

    return bankTransferPayment;
  }

  private String createInvoice(Investment investment, String paymentType) {
    Map<String, Object> documentsParameters = templateDataUtil.getInvoiceData(investment, paymentType);
    byte[] file = pdfService.generatePdf(documentsParameters, FileType.INVOICE);

    String url = fileService.uploadFile(file, "pdf");

    sendInvoiceEmail(investment, file);
    return url;
  }

  private void sendInvoiceEmail(Investment investment, byte[] file) {
    if (investment.getPerson().getEmail() != null) {
      Map<String, Object> content = new HashMap<>();
      content.put(Parameters.CAMPAIGN, investment.getCampaign().getName());
      content.put(Parameters.FIRST_NAME, investment.getPerson().getFirstName());
      content.put(Parameters.LAST_NAME, investment.getPerson().getLastName());
      content.put(Parameters.INVESTMENT, investment);
      LocalDateTime paymentDate =
          LocalDateTime.ofInstant(investment.getPaymentDate(), ZoneId.of(timeZone));
      content.put(
          Parameters.INVOICE_NUMBER,
          investment.getCurrency() + "/" + paymentDate.getYear() + "-" + investment.getId());

      emailService.sendEmailToUser(
          EmailType.INVOICE,
          Collections.singletonList(investment.getPerson().getEmail()),
          content,
          new EmailAttachment(file, "Invoice", ".pdf"));
    }
  }

  @Override
  public String getInvoice(Long investmentId) {
    Investment investment = investmentRepository.getOne(investmentId);

    throwIfNoAccess(investment);
    if (!investment.getInvestmentState().getName().equals(InvestmentStateName.PAID)
        && !investment.getInvestmentState().getName().equals(InvestmentStateName.AUDIT_APPROVED)) {
      throw new BadRequestException(INVESTMENT_NOT_PAID);
    }

    return investment.getInvoiceUrl();
  }

  @Transactional
  @Override
  public PayPalPayment confirmPayPalPayment(String orderId, Long investmentId) {
    Investment investment = investmentService.findByIdOrThrowException(investmentId);
    throwIfInvestmentAlreadyPaid(investment);

    Order order = payPalClient.getOrder(orderId);
    BigDecimal payPalAmount =
        new BigDecimal(order.purchaseUnits().get(0).amountWithBreakdown().value());

    // TODO revisit when we're sure about PayPal fees
    //    throwIfAmountsNotEqual(payPalAmount, investment.getInvestedAmount());

    payPalClient.captureRequest(orderId);

    investment.setInvestmentState(
        investmentStateService.getInvestmentState(InvestmentStateName.PAID));
    investment.setPaymentDate(Instant.now());
    investment.setInvoiceUrl(createInvoice(investment, PAYPAL_PAYMENT_TYPE));
    investmentService.save(investment);

    // TODO remove two calls beneath after demo
    investmentRepository.flush();
    investmentService.auditorApproveInvestment(investmentId);

    PayPalPayment payPalPayment =
        createPayPalPayment(investment, payPalAmount, orderId, order.createTime());

    blockchainMessageProducer.produceMessage(
        BlockchainMethod.PAYMENT_CONFIRMED,
        new PaymentDto(payPalPayment, AuthenticationUtil.getAuthentication().getAuth().getId()),
        AuthenticationUtil.getAuthentication().getAuth().getUsername(),
        AuthenticationUtil.getClientIp());

    return payPalPayment;
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

  private void throwIfNotAllowedFilter(String filter) {
    if (filter == null) {
      return;
    }
    if (!(filter.equalsIgnoreCase(InvestmentStateName.OWNER_APPROVED.toString())
        || filter.equalsIgnoreCase(InvestmentStateName.PAID.toString())
        || filter.equalsIgnoreCase(InvestmentStateName.EXPIRED.toString()))) {
      throw new BadRequestException(ExceptionMessages.INVALID_REQUEST);
    }
  }

  private void throwIfNoAccess(Investment investment) {
    Auth auth = AuthenticationUtil.getAuthentication().getAuth();
    if (!AuthenticationUtil.hasUserAdminRole()
        && !investment.getPerson().getId().equals(auth.getPerson().getId())) {
      throw new BadRequestException(INVALID_REQUEST);
    }
  }

  private Optional<BankTransferPayment> findBankTransferPaymentForInvestment(Long investmentId) {
    return bankTransferPaymentRepository.findByInvestmentId(investmentId);
  }

  private BankTransferPayment findBankTransferPaymentForInvestmentOrThrow(Long investmentId) {
    return bankTransferPaymentRepository
        .findByInvestmentId(investmentId)
        .orElseThrow(EntityNotFoundException::new);
  }
}
