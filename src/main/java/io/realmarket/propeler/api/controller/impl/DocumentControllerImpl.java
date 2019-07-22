package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.DocumentController;
import io.realmarket.propeler.model.DocumentMetadata;
import io.realmarket.propeler.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/{entity}/documents")
public class DocumentControllerImpl implements DocumentController {

  private final DocumentService documentService;

  @Autowired
  public DocumentControllerImpl(DocumentService documentService) {
    this.documentService = documentService;
  }

  @Override
  @GetMapping(value = "/types")
  public ResponseEntity<List<DocumentMetadata>> getDocumentTypes(@PathVariable String entity) {
    return ResponseEntity.ok(documentService.getTypes(entity));
  }
}
