package io.realmarket.propeler.service.impl;

import com.paypal.orders.Order;
import io.realmarket.propeler.model.BankTransferPayment;
import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.PayPalPayment;
import io.realmarket.propeler.model.Payment;
import io.realmarket.propeler.model.enums.InvestmentStateName;
import io.realmarket.propeler.repository.BankTransferPaymentRepository;
import io.realmarket.propeler.repository.InvestmentRepository;
import io.realmarket.propeler.repository.PayPalPaymentRepository;
import io.realmarket.propeler.service.*;
import io.realmarket.propeler.service.blockchain.queue.BlockchainMessageProducer;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.payment.PayPalClient;
import io.realmarket.propeler.service.util.MailContentHolder;
import io.realmarket.propeler.service.util.PdfService;
import io.realmarket.propeler.service.util.TemplateDataUtil;
import io.realmarket.propeler.util.InvestmentUtils;
import io.realmarket.propeler.util.PaymentUtils;
import io.realmarket.propeler.util.PlatformSettingsUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Optional;

import static io.realmarket.propeler.util.AuthUtils.mockRequestAndContext;
import static io.realmarket.propeler.util.InvestmentUtils.TEST_INVESTMENT_PAID;
import static io.realmarket.propeler.util.PaymentUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class PaymentServiceImplTest {

  @InjectMocks PaymentServiceImpl paymentService;
  @Mock private PaymentDocumentService paymentDocumentService;
  @Mock private InvestmentStateService investmentStateService;
  @Mock private InvestmentService investmentService;
  @Mock private PayPalPaymentRepository payPalPaymentRepository;
  @Mock private PayPalClient payPalClient;
  @Mock private PlatformSettingsService platformSettingsService;
  @Mock private PdfService pdfService;
  @Mock private FileService fileService;
  @Mock private EmailService emailService;
  @Mock private InvestmentRepository investmentRepository;
  @Mock private BankTransferPaymentRepository bankTransferPaymentRepository;
  @Mock private BlockchainMessageProducer blockchainMessageProducer;
  @Mock private TemplateDataUtil templateDataUtil;

  @Before
  public void createAuthContext() {
    mockRequestAndContext();
    ReflectionTestUtils.setField(paymentService, "timeZone", "Europe/Belgrade");
  }

  @Test
  public void GetBankTransferPayment_Should_ReturnPayment() {
    Investment ownerApprovedInvestment = InvestmentUtils.mockOwnerApprovedInvestment();
    BankTransferPayment bankTransferPayment = mockPaidBankTransferPayment();

    when(investmentRepository.getOne(InvestmentUtils.INVESTMENT_ID))
        .thenReturn(ownerApprovedInvestment);
    when(bankTransferPaymentRepository.findByInvestmentId(InvestmentUtils.INVESTMENT_ID))
        .thenReturn(Optional.of(bankTransferPayment));

    BankTransferPayment retVal =
        paymentService.getBankTransferPayment(InvestmentUtils.INVESTMENT_ID);

    assertNotNull(retVal);
  }

  @Test
  public void GetBankTransferPayment_Should_CreateAndReturnPayment() {
    Investment ownerApprovedInvestment = InvestmentUtils.mockOwnerApprovedInvestment();
    BankTransferPayment bankTransferPayment = mockPaidBankTransferPayment();

    when(investmentRepository.getOne(InvestmentUtils.INVESTMENT_ID))
        .thenReturn(ownerApprovedInvestment);
    when(bankTransferPaymentRepository.findByInvestmentId(InvestmentUtils.INVESTMENT_ID))
        .thenReturn(Optional.ofNullable(null));
    when(platformSettingsService.getPlatformCurrency())
        .thenReturn(PlatformSettingsUtils.TEST_PLATFORM_CURRENCY);
    when(fileService.uploadFile(any(), anyString())).thenReturn(TEST_PROFORMA_INVOICE_URL);
    doNothing().when(emailService).sendMailToUser(any(MailContentHolder.class));
    when(bankTransferPaymentRepository.save(any())).thenReturn(bankTransferPayment);

    BankTransferPayment retVal =
        paymentService.getBankTransferPayment(InvestmentUtils.INVESTMENT_ID);

    verify(bankTransferPaymentRepository, Mockito.times(1)).save(any());
    assertNotNull(retVal);
  }

  @Test
  public void GetPayments_Should_ReturnPayments() {
    Pageable pageable = Mockito.mock(Pageable.class);
    Investment ownerApprovedInvestment = InvestmentUtils.mockOwnerApprovedInvestment();
    Page<Investment> page = new PageImpl<>(Collections.singletonList(ownerApprovedInvestment));

    when(investmentRepository.findAllPaymentInvestment(null, pageable)).thenReturn(page);

    paymentService.getPayments(pageable, null);

    verify(investmentRepository, Mockito.times(1)).findAllPaymentInvestment(null, pageable);
  }

  @Test
  public void GetPaymentsWithFilter_Should_ReturnPayments() {
    Pageable pageable = Mockito.mock(Pageable.class);
    Investment ownerApprovedInvestment = InvestmentUtils.mockOwnerApprovedInvestment();
    Page<Investment> page = new PageImpl<>(Collections.singletonList(ownerApprovedInvestment));

    when(investmentRepository.findAllPaymentInvestment(
            InvestmentStateName.OWNER_APPROVED, pageable))
        .thenReturn(page);

    paymentService.getPayments(pageable, "owner_approved");

    verify(investmentRepository, Mockito.times(1))
        .findAllPaymentInvestment(InvestmentStateName.OWNER_APPROVED, pageable);
  }

  @Test(expected = BadRequestException.class)
  public void GetPayments_Should_Throw_BadRequestException() {
    Pageable pageable = Mockito.mock(Pageable.class);

    paymentService.getPayments(pageable, "wrong_filter");
  }

  @Test
  public void confirmBankTransferPayment_Should_ConfirmPayment() {
    BankTransferPayment paidBankTransferPayment = mockPaidBankTransferPayment();
    Investment ownerApprovedInvestment = InvestmentUtils.mockOwnerApprovedInvestment();

    when(investmentRepository.getOne(TEST_ID)).thenReturn(ownerApprovedInvestment);
    when(investmentStateService.getInvestmentState(InvestmentStateName.OWNER_APPROVED))
        .thenReturn(InvestmentUtils.TEST_INVESTMENT_OWNER_APPROVED_STATE);
    when(bankTransferPaymentRepository.findByInvestmentId(InvestmentUtils.INVESTMENT_ID))
        .thenReturn(Optional.of(paidBankTransferPayment));
    when(fileService.uploadFile(any(), anyString())).thenReturn(InvestmentUtils.TEST_INVOICE_URL);
    doNothing().when(emailService).sendMailToUser(any(MailContentHolder.class));
    when(investmentRepository.save(any())).thenReturn(InvestmentUtils.TEST_INVESTMENT_PAID);
    when(bankTransferPaymentRepository.save(any())).thenReturn(paidBankTransferPayment);

    Payment retVal =
        paymentService.confirmBankTransferPayment(TEST_ID, TEST_PAYMENT_CONFIRMATION_DTO);

    assertNotNull(retVal);
    verify(bankTransferPaymentRepository, Mockito.times(1)).save(any(BankTransferPayment.class));
  }

  @Test
  public void confirmPayPalPayment_Should_ConfirmPayment() {
    Investment mockOwnerApprovedInvestment = InvestmentUtils.mockOwnerApprovedInvestment();
    Order testOrder = PaymentUtils.getTestOrder();

    when(investmentService.findByIdOrThrowException(TEST_ID))
        .thenReturn(mockOwnerApprovedInvestment);

    when(payPalClient.getOrder(TEST_PAYPAL_ORDER_ID)).thenReturn(testOrder);
    when(investmentStateService.getInvestmentState(InvestmentStateName.PAID))
        .thenReturn(InvestmentUtils.TEST_INVESTMENT_PAID_STATE);
    when(investmentService.save(any())).thenReturn(mockOwnerApprovedInvestment);
    when(platformSettingsService.getPlatformCurrency())
        .thenReturn(PlatformSettingsUtils.TEST_PLATFORM_CURRENCY);
    when(payPalPaymentRepository.save(any())).thenReturn(TEST_PAYPAL_PAYMENT);

    PayPalPayment actualPayPalPayment =
        paymentService.confirmPayPalPayment(TEST_PAYPAL_ORDER_ID, TEST_ID);
    assertEquals(actualPayPalPayment.getPayPalOrderId(), (TEST_PAYPAL_ORDER_ID));
    assertEquals(actualPayPalPayment.getInvestment().getId(), mockOwnerApprovedInvestment.getId());
    verify(payPalClient, times(1)).getOrder(TEST_PAYPAL_ORDER_ID);
    verify(payPalClient, times(1)).captureRequest(TEST_PAYPAL_ORDER_ID);
  }

  @Test(expected = BadRequestException.class)
  public void confirmPayPalPayment_Should_Throw_Exception_When_Payment_Exists() {

    when(investmentService.findByIdOrThrowException(TEST_ID)).thenReturn(TEST_INVESTMENT_PAID);

    when(payPalPaymentRepository.findByInvestmentId(TEST_ID)).thenThrow(BadRequestException.class);

    paymentService.confirmPayPalPayment(TEST_PAYPAL_ORDER_ID, TEST_ID);
  }

  // revisit when we're sure about PayPal fees
  //  @Test(expected = AmountsNotEqualException.class)
  //  public void confirmPayPalPayment_Should_Throw_AmountsNotEqualException() {
  //    Investment mockOwnerApprovedInvestment = InvestmentUtils.mockOwnerApprovedInvestment();
  //    Order testOrder = PaymentUtils.getTestOrderWithWrongAmount();
  //
  //    when(investmentService.findByIdOrThrowException(TEST_ID))
  //        .thenReturn(mockOwnerApprovedInvestment);
  //
  //    when(payPalClient.getOrder(TEST_PAYPAL_ORDER_ID)).thenReturn(testOrder);
  //
  //    paymentService.confirmPayPalPayment(TEST_PAYPAL_ORDER_ID, TEST_ID);
  //
  //    verify(payPalClient, times(1)).getOrder(TEST_PAYPAL_ORDER_ID);
  //  }

  @Test(expected = BadRequestException.class)
  public void confirmBankTransferPayment_Should_Throw_BadRequestException() {
    when(investmentRepository.getOne(TEST_ID)).thenReturn(TEST_INVESTMENT_PAID);

    paymentService.confirmBankTransferPayment(TEST_ID, TEST_PAYMENT_CONFIRMATION_DTO);
  }

  @Test()
  public void getInvoice_Should_ReturnInvoice() {
    Investment paidInvestment = InvestmentUtils.mockPaidInvestment();

    when(investmentRepository.getOne(TEST_ID)).thenReturn(paidInvestment);

    String retVal = paymentService.getInvoice(InvestmentUtils.INVESTMENT_ID);

    assertNotNull(retVal);
  }
}
