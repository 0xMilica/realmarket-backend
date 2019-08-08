package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.KYCDocumentDto;
import io.realmarket.propeler.model.UserKYC;
import io.realmarket.propeler.model.UserKYCDocument;

import java.util.List;

public interface UserKYCDocumentService {

  UserKYC submitDocuments(UserKYC userKYC, List<KYCDocumentDto> documents);

  List<UserKYCDocument> findByUserKYC(UserKYC userKYC);
}
