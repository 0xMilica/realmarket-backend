package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Investment;
import io.realmarket.propeler.model.enums.InvestmentStateName;
import io.realmarket.propeler.repository.InvestmentRepository;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.util.InvestmentUtils;
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

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class PaymentServiceImplTest {

  @Mock private InvestmentRepository investmentRepository;

  @InjectMocks PaymentServiceImpl paymentService;

  @Test
  public void GetPayments_Should_ReturnPayments() {
    Pageable pageable = Mockito.mock(Pageable.class);
    Page<Investment> page =
        new PageImpl<>(Collections.singletonList(InvestmentUtils.TEST_INVESTMENT_OWNER_APPROVED));

    when(investmentRepository.findAllPaymentInvestment(null, pageable)).thenReturn(page);

    paymentService.getPayments(pageable, null);

    verify(investmentRepository, Mockito.times(1)).findAllPaymentInvestment(null, pageable);
  }

  @Test
  public void GetPaymentsWithFilter_Should_ReturnPayments() {
    Pageable pageable = Mockito.mock(Pageable.class);
    Page<Investment> page =
        new PageImpl<>(Collections.singletonList(InvestmentUtils.TEST_INVESTMENT_OWNER_APPROVED));

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
}
