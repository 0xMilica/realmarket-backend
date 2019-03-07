package io.realmarket.propeler.service;

import io.realmarket.propeler.model.CampaignDocument;

public interface CampaignDocumentService {

  CampaignDocument submitDocument(
      CampaignDocument campaignDocument, String campaignUrlFriendlyName);

  void deleteDocument(String campaignUrlFriendlyName, Long documentId);

  CampaignDocument findByIdOrThrowException(Long documentId);
}
