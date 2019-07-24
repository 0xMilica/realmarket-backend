package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.DocumentDto;
import io.realmarket.propeler.model.Document;

public interface PersonDocumentService {
  Document submitPersonDocument(DocumentDto documentDto);
}
