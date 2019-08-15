package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.BankTransferPayment;
import io.realmarket.propeler.model.Document;
import io.realmarket.propeler.model.PaymentDocument;
import io.realmarket.propeler.repository.DocumentAccessLevelRepository;
import io.realmarket.propeler.repository.DocumentTypeRepository;
import io.realmarket.propeler.repository.PaymentDocumentRepository;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.util.AuthUtils;
import io.realmarket.propeler.util.PaymentUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static io.realmarket.propeler.util.PaymentDocumentUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class PaymentDocumentServiceImplTest {

  @Mock private CloudObjectStorageService cloudObjectStorageService;
  @Mock private DocumentAccessLevelRepository documentAccessLevelRepository;
  @Mock private DocumentTypeRepository documentTypeRepository;
  @Mock private PaymentDocumentRepository paymentDocumentRepository;

  @InjectMocks private PaymentDocumentServiceImpl paymentDocumentService;

  @Before
  public void createAuthContext() {
    AuthUtils.mockRequestAndContextAdmin();
  }

  @Test
  public void submitDocument_Should_CreateNewDocument() {
    BankTransferPayment paidBankTransferPayment = PaymentUtils.mockPaidBankTransferPayment();
    PaymentDocument paymentDocument = mockPaymentDocument();

    when(documentAccessLevelRepository.findByName(TEST_ACCESS_LEVEL_ENUM))
        .thenReturn(Optional.of(TEST_ACCESS_LEVEL));
    when(documentTypeRepository.findByName(TEST_TYPE_ENUM)).thenReturn(Optional.of(TEST_TYPE));
    when(cloudObjectStorageService.doesFileExist(paymentDocument.getUrl())).thenReturn(true);
    when(paymentDocumentRepository.save(any())).thenReturn(paymentDocument);

    Document retVal =
        paymentDocumentService.submitDocument(paidBankTransferPayment, TEST_URL, TEST_TITLE);

    assertEquals(TEST_URL, retVal.getUrl());
    verify(paymentDocumentRepository, Mockito.times(1)).save(any(PaymentDocument.class));
  }
}
