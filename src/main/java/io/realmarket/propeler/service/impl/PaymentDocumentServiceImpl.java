package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.*;
import io.realmarket.propeler.model.enums.DocumentAccessLevelName;
import io.realmarket.propeler.model.enums.DocumentTypeName;
import io.realmarket.propeler.repository.DocumentAccessLevelRepository;
import io.realmarket.propeler.repository.DocumentTypeRepository;
import io.realmarket.propeler.repository.PaymentDocumentRepository;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.PaymentDocumentService;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;

@Service
public class PaymentDocumentServiceImpl implements PaymentDocumentService {

  private final DocumentAccessLevelRepository documentAccessLevelRepository;
  private final DocumentTypeRepository documentTypeRepository;
  private final CloudObjectStorageService cloudObjectStorageService;
  private final PaymentDocumentRepository paymentDocumentRepository;

  @Autowired
  public PaymentDocumentServiceImpl(
      DocumentAccessLevelRepository documentAccessLevelRepository,
      DocumentTypeRepository documentTypeRepository,
      CloudObjectStorageService cloudObjectStorageService,
      PaymentDocumentRepository paymentDocumentRepository) {
    this.documentAccessLevelRepository = documentAccessLevelRepository;
    this.documentTypeRepository = documentTypeRepository;
    this.cloudObjectStorageService = cloudObjectStorageService;
    this.paymentDocumentRepository = paymentDocumentRepository;
  }

  @Transactional
  @Override
  public Document submitDocument(Payment payment, String documentUrl, String documentTitle) {
    PaymentDocument paymentDocument = createDocument(payment, documentUrl, documentTitle);
    paymentDocument.setUploadDate(Instant.now());

    if (!cloudObjectStorageService.doesFileExist(paymentDocument.getUrl())) {
      throw new EntityNotFoundException(ExceptionMessages.FILE_DOES_NOT_EXIST);
    }

    return paymentDocumentRepository.save(paymentDocument);
  }

  private PaymentDocument createDocument(
      Payment payment, String documentUrl, String documentTitle) {
    DocumentAccessLevel accessLevel =
        this.documentAccessLevelRepository
            .findByName(DocumentAccessLevelName.PRIVATE)
            .orElseThrow(EntityNotFoundException::new);
    DocumentType type =
        this.documentTypeRepository
            .findByName(DocumentTypeName.PAYMENT_PROOF)
            .orElseThrow(EntityNotFoundException::new);

    return PaymentDocument.paymentDocumentBuilder()
        .title(documentTitle)
        .accessLevel(accessLevel)
        .type(type)
        .url(documentUrl)
        .payment(payment)
        .build();
  }
}
