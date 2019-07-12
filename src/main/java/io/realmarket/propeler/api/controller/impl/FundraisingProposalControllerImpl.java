package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.annotations.RequireCaptcha;
import io.realmarket.propeler.api.controller.FundraisingProposalController;
import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.service.FundraisingProposalDocumentService;
import io.realmarket.propeler.service.FundraisingProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/fundraisingProposals")
public class FundraisingProposalControllerImpl implements FundraisingProposalController {

  private final FundraisingProposalService fundraisingProposalService;
  private final FundraisingProposalDocumentService fundraisingProposalDocumentService;

  @Autowired
  public FundraisingProposalControllerImpl(
      FundraisingProposalService fundraisingProposalService,
      FundraisingProposalDocumentService fundraisingProposalDocumentService) {
    this.fundraisingProposalService = fundraisingProposalService;
    this.fundraisingProposalDocumentService = fundraisingProposalDocumentService;
  }

  @Override
  @PostMapping
  @RequireCaptcha
  public ResponseEntity<FundraisingProposalResponseDto> makeFundraisingProposal(
      @RequestBody FundraisingProposalDto fundraisingProposalDto) {
    return ResponseEntity.ok(
        new FundraisingProposalResponseDto(
            fundraisingProposalService.applyForFundraising(fundraisingProposalDto)));
  }

  @Override
  @GetMapping("/{fundraisingProposalId}")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<FundraisingProposalResponseDto> getFundraisingProposal(
      @PathVariable Long fundraisingProposalId) {
    return ResponseEntity.ok(
        new FundraisingProposalResponseDto(
            fundraisingProposalService.findByIdOrThrowException(fundraisingProposalId)));
  }

  @Override
  @GetMapping
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Page<FundraisingProposalResponseDto>> getFundraisingProposals(
      Pageable pageable,
      @RequestParam(value = "filter", required = false, defaultValue = "all") String filter) {
    return ResponseEntity.ok(
        fundraisingProposalService
            .getFundraisingProposalsByState(pageable, filter)
            .map(FundraisingProposalResponseDto::new));
  }

  @Override
  @PostMapping(value = "/{fundraisingProposalId}/documents")
  public ResponseEntity<FundraisingProposalDocumentResponseDto> submitFundraisingProposalDocument(
      @RequestBody @Valid FundraisingProposalDocumentDto fundraisingProposalDocumentDto,
      @PathVariable Long fundraisingProposalId) {
    return ResponseEntity.ok(
        new FundraisingProposalDocumentResponseDto(
            fundraisingProposalDocumentService.submitDocument(
                fundraisingProposalDocumentDto, fundraisingProposalId)));
  }

  @Override
  @GetMapping(value = "/{fundraisingProposalId}/documents")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<List<FundraisingProposalDocumentResponseDto>>
      getFundraisingProposalDocuments(@PathVariable Long fundraisingProposalId) {
    return ResponseEntity.ok(
        fundraisingProposalDocumentService.getFundraisingProposalDocuments(fundraisingProposalId));
  }

  @Override
  @PatchMapping("/{fundraisingProposalId}/accept")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<FundraisingProposalResponseDto> acceptFundraisingProposal(
      @PathVariable Long fundraisingProposalId) {
    return ResponseEntity.ok(
        new FundraisingProposalResponseDto(
            fundraisingProposalService.approveFundraisingProposal(fundraisingProposalId)));
  }

  @Override
  @PatchMapping("/{fundraisingProposalId}/reject")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<FundraisingProposalResponseDto> rejectFundraisingProposal(
      @PathVariable Long fundraisingProposalId, @RequestBody AuditDeclineDto auditDeclineDto) {
    return ResponseEntity.ok(
        new FundraisingProposalResponseDto(
            fundraisingProposalService.declineFundraisingProposal(
                fundraisingProposalId, auditDeclineDto)));
  }
}
