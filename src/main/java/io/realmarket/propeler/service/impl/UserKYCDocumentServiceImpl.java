package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.DocumentAccessLevel;
import io.realmarket.propeler.model.DocumentType;
import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.model.UserKYCDocument;
import io.realmarket.propeler.model.enums.DocumentAccessLevelName;
import io.realmarket.propeler.model.enums.DocumentTypeName;
import io.realmarket.propeler.repository.DocumentAccessLevelRepository;
import io.realmarket.propeler.repository.DocumentTypeRepository;
import io.realmarket.propeler.repository.UserKYCDocumentRepository;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.UserKYCDocumentService;
import io.realmarket.propeler.service.UserKYCService;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;

@Service
public class UserKYCDocumentServiceImpl implements UserKYCDocumentService {

  private final UserKYCService userKYCService;
  private final CloudObjectStorageService cloudObjectStorageService;
  private final DocumentAccessLevelRepository documentAccessLevelRepository;
  private final DocumentTypeRepository documentTypeRepository;
  private final UserKYCDocumentRepository userKYCDocumentRepository;

  @Autowired
  public UserKYCDocumentServiceImpl(
      @Lazy UserKYCService userKYCService,
      CloudObjectStorageService cloudObjectStorageService,
      DocumentAccessLevelRepository documentAccessLevelRepository,
      DocumentTypeRepository documentTypeRepository,
      UserKYCDocumentRepository userKYCDocumentRepository) {
    this.userKYCService = userKYCService;
    this.cloudObjectStorageService = cloudObjectStorageService;
    this.documentAccessLevelRepository = documentAccessLevelRepository;
    this.documentTypeRepository = documentTypeRepository;
    this.userKYCDocumentRepository = userKYCDocumentRepository;
  }

  @Override
  public UserKYC submitDocuments(UserKYC userKYC, List<String> documentUrls) {
    for (String documentUrl : documentUrls) {
      UserKYCDocument userKYCDocument = convertDocumentDtoToDocument(documentUrl, userKYC);
      userKYCDocument.setUploadDate(Instant.now());

      if (!cloudObjectStorageService.doesFileExist(userKYCDocument.getUrl())) {
        throw new EntityNotFoundException(ExceptionMessages.FILE_DOES_NOT_EXIST);
      }

      userKYCDocumentRepository.save(userKYCDocument);
    }
    return userKYC;
  }

    @Override
    public List<UserKYCDocument> findByUserKYC(UserKYC userKYC) {
        return userKYCDocumentRepository.findAllByUserKYC(userKYC);
    }

    private UserKYCDocument convertDocumentDtoToDocument(String documentUrl, UserKYC userKYC) {
    DocumentAccessLevel accessLevel =
        this.documentAccessLevelRepository
            .findByName(DocumentAccessLevelName.PRIVATE)
            .orElseThrow(EntityNotFoundException::new);
    DocumentType type =
        this.documentTypeRepository
            .findByName(DocumentTypeName.USER_KYC)
            .orElseThrow(EntityNotFoundException::new);

    return UserKYCDocument.userKYCDocumentBuilder()
        .accessLevel(accessLevel)
        .type(type)
        .url(documentUrl)
        .userKYC(userKYC)
        .build();
  }
}
