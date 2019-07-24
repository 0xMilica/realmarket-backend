package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.DocumentMetadata;
import io.realmarket.propeler.service.DocumentService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.DocumentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

  @Autowired
  public DocumentServiceImpl() {}

  @Override
  public List<DocumentMetadata> getTypes(String entity) {
    if (DocumentUtil.documentEntityTypes.containsKey(entity)) {
      return DocumentUtil.documentEntityTypes.get(entity);
    }
    throw new BadRequestException(ExceptionMessages.INVALID_REQUEST);
  }
}
