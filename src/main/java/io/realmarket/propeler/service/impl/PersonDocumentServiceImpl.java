package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.DocumentDto;
import io.realmarket.propeler.model.*;
import io.realmarket.propeler.repository.DocumentAccessLevelRepository;
import io.realmarket.propeler.repository.DocumentTypeRepository;
import io.realmarket.propeler.repository.PersonDocumentRepository;
import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.PersonDocumentService;
import io.realmarket.propeler.service.PersonService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;

@Transactional
@Service
public class PersonDocumentServiceImpl implements PersonDocumentService {
  private final DocumentAccessLevelRepository documentAccessLevelRepository;
  private final DocumentTypeRepository documentTypeRepository;
  private final CloudObjectStorageService cloudObjectStorageService;
  private final PersonDocumentRepository personDocumentRepository;
  private final PersonService personService;

  public PersonDocumentServiceImpl(
      DocumentAccessLevelRepository documentAccessLevelRepository,
      DocumentTypeRepository documentTypeRepository,
      CloudObjectStorageService cloudObjectStorageService,
      PersonDocumentRepository personDocumentRepository,
      PersonService personService) {
    this.documentAccessLevelRepository = documentAccessLevelRepository;
    this.documentTypeRepository = documentTypeRepository;
    this.cloudObjectStorageService = cloudObjectStorageService;
    this.personDocumentRepository = personDocumentRepository;
    this.personService = personService;
  }

  @Override
  @Transactional
  public PersonDocument submitPersonDocument(DocumentDto documentDto) {
    Person person = personService.getPersonFromAuth(AuthenticationUtil.getAuthentication().getAuth());

    PersonDocument personDocument = convertDocumentDtoToDocument(documentDto, person);
    personDocument.setUploadDate(Instant.now());

    if (!cloudObjectStorageService.doesFileExist(personDocument.getUrl())) {
      throw new EntityNotFoundException(ExceptionMessages.FILE_DOES_NOT_EXIST);
    }

    return personDocumentRepository.save(personDocument);
  }

  private PersonDocument convertDocumentDtoToDocument(DocumentDto documentDto, Person person) {
    Optional<DocumentAccessLevel> accessLevel =
        this.documentAccessLevelRepository.findByName(documentDto.getAccessLevel());
    Optional<DocumentType> type =
        this.documentTypeRepository.findByName(documentDto.getType());
    if (!accessLevel.isPresent() || !type.isPresent()) {
      throw new BadRequestException(ExceptionMessages.INVALID_REQUEST);
    }

    return PersonDocument.personDocumentBuilder()
        .title(documentDto.getTitle())
        .accessLevel(accessLevel.get())
        .type(type.get())
        .url(documentDto.getUrl())
        .person(person)
        .build();
  }
}
