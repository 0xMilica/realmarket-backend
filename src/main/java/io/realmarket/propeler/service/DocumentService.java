package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.DocumentResponseDto;

import java.util.List;

public interface DocumentService {
  List<DocumentResponseDto> getDocuments(Long userId);
}
