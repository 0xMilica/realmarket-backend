package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.BankTransferPayment;
import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.Payment;
import io.realmarket.propeler.model.enums.InvestmentStateName;
import io.realmarket.propeler.repository.BankTransferPaymentRepository;
import io.realmarket.propeler.repository.InvestmentRepository;
import io.realmarket.propeler.service.InvestmentStateService;
import io.realmarket.propeler.service.PaymentDocumentService;
import io.realmarket.propeler.service.PlatformSettingsService;
import io.realmarket.propeler.service.blockchain.BlockchainCommunicationService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.util.InvestmentUtils;
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

import java.util.Collections;
import java.util.Optional;

import static io.realmarket.propeler.util.AuthUtils.mockRequestAndContext;
import static io.realmarket.propeler.util.PaymentUtils.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class PaymentServiceImplTest {

  @Mock private PaymentDocumentService paymentDocumentService;
  @Mock private InvestmentStateService investmentStateService;
  @Mock private PlatformSettingsService platformSettingsService;
  @Mock private InvestmentRepository investmentRepository;
  @Mock private BankTransferPaymentRepository bankTransferPaymentRepository;
  @Mock private BlockchainCommunicationService blockchainCommunicationService;

  @InjectMocks PaymentServiceImpl paymentService;

  @Before
  public void createAuthContext() {
    mockRequestAndContext();
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
    when(investmentRepository.save(any())).thenReturn(InvestmentUtils.TEST_INVESTMENT_PAID);
    when(bankTransferPaymentRepository.save(any())).thenReturn(paidBankTransferPayment);

    Payment retVal =
        paymentService.confirmBankTransferPayment(TEST_ID, TEST_PAYMENT_CONFIRMATION_DTO);

    assertNotNull(retVal);
    verify(bankTransferPaymentRepository, Mockito.times(1)).save(any(BankTransferPayment.class));
  }

  @Test(expected = BadRequestException.class)
  public void confirmBankTransferPayment_Should_Throw_BadRequestException() {
    when(investmentRepository.getOne(TEST_ID)).thenReturn(InvestmentUtils.TEST_INVESTMENT_PAID);

    paymentService.confirmBankTransferPayment(TEST_ID, TEST_PAYMENT_CONFIRMATION_DTO);
  }
}
