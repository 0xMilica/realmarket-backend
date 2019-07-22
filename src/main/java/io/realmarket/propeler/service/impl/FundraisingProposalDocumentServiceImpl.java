package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.FundraisingProposalDocumentDto;
import io.realmarket.propeler.api.dto.FundraisingProposalDocumentResponseDto;
import io.realmarket.propeler.model.DocumentType;
import io.realmarket.propeler.model.FundraisingProposal;
import io.realmarket.propeler.model.FundraisingProposalDocument;
import io.realmarket.propeler.repository.DocumentTypeRepository;
import io.realmarket.propeler.repository.FundraisingProposalDocumentRepository;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.FundraisingProposalDocumentService;
import io.realmarket.propeler.service.FundraisingProposalService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FundraisingProposalDocumentServiceImpl implements FundraisingProposalDocumentService {

  private FundraisingProposalService fundraisingProposalService;
  private FundraisingProposalDocumentRepository fundraisingProposalDocumentRepository;
  private DocumentTypeRepository documentTypeRepository;
  private CloudObjectStorageService cloudObjectStorageService;

  @Autowired
  public FundraisingProposalDocumentServiceImpl(
      FundraisingProposalService fundraisingProposalService,
      FundraisingProposalDocumentRepository fundraisingProposalDocumentRepository,
      DocumentTypeRepository documentTypeRepository,
      CloudObjectStorageService cloudObjectStorageService) {
    this.fundraisingProposalService = fundraisingProposalService;
    this.fundraisingProposalDocumentRepository = fundraisingProposalDocumentRepository;
    this.documentTypeRepository = documentTypeRepository;
    this.cloudObjectStorageService = cloudObjectStorageService;
  }

  @Override
  public FundraisingProposalDocument findByIdOrThrowException(Long documentId) {
    return fundraisingProposalDocumentRepository
        .findById(documentId)
        .orElseThrow(EntityNotFoundException::new);
  }

  @Override
  public List<FundraisingProposalDocument> findAllByFundraisingProposal(
      FundraisingProposal fundraisingProposal) {
    return fundraisingProposalDocumentRepository.findAllByFundraisingProposal(fundraisingProposal);
  }

  @Override
  @Transactional
  public FundraisingProposalDocument submitDocument(
      FundraisingProposalDocumentDto fundraisingProposalDocumentDto, Long proposalId) {
    FundraisingProposal fundraisingProposal =
        fundraisingProposalService.findByIdOrThrowException(proposalId);

    FundraisingProposalDocument fundraisingProposalDocument =
        convertDocumentDtoToDocument(fundraisingProposalDocumentDto, fundraisingProposal);
    fundraisingProposalDocument.setUploadDate(Instant.now());

    if (!cloudObjectStorageService.doesFileExist(fundraisingProposalDocument.getUrl())) {
      throw new EntityNotFoundException(ExceptionMessages.FILE_DOES_NOT_EXIST);
    }

    return fundraisingProposalDocumentRepository.save(fundraisingProposalDocument);
  }

  @Override
  public List<FundraisingProposalDocumentResponseDto> getFundraisingProposalDocuments(
      Long proposalId) {
    FundraisingProposal fundraisingProposal =
        fundraisingProposalService.findByIdOrThrowException(proposalId);

    return findAllByFundraisingProposal(fundraisingProposal).stream()
        .map(FundraisingProposalDocumentResponseDto::new)
        .collect(Collectors.toList());
  }

  private FundraisingProposalDocument convertDocumentDtoToDocument(
      FundraisingProposalDocumentDto fundraisingProposalDocumentDto,
      FundraisingProposal fundraisingProposal) {
    Optional<DocumentType> type =
        this.documentTypeRepository.findByName(fundraisingProposalDocumentDto.getType());
    if (!type.isPresent()) {
      throw new BadRequestException(ExceptionMessages.INVALID_REQUEST);
    }

    return FundraisingProposalDocument.fundraisingProposalDocumentBuilder()
        .title(fundraisingProposalDocumentDto.getTitle())
        .type(type.get())
        .url(fundraisingProposalDocumentDto.getUrl())
        .fundraisingProposal(fundraisingProposal)
        .build();
  }
}
