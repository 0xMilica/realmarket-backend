package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.DocumentDto;
import io.realmarket.propeler.model.Document;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.model.PersonDocument;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonDocumentService {
  Document submitPersonDocument(DocumentDto documentDto);

  List<PersonDocument> findByPerson(Person person);
}
