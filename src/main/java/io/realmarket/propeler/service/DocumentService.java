package io.realmarket.propeler.service;

import io.realmarket.propeler.model.DocumentMetadata;

import java.util.List;

public interface DocumentService {

  List<DocumentMetadata> getTypes(String entity);
}
