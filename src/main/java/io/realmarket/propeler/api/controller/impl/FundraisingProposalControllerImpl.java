package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.annotations.RequireCaptcha;
import io.realmarket.propeler.api.controller.FundraisingProposalController;
import io.realmarket.propeler.api.dto.FundraisingProposalDto;
import io.realmarket.propeler.api.dto.FundraisingProposalResponseDto;
import io.realmarket.propeler.service.FundraisingProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fundraisingProposals")
public class FundraisingProposalControllerImpl implements FundraisingProposalController {

  private final FundraisingProposalService fundraisingProposalService;

  @Autowired
  public FundraisingProposalControllerImpl(FundraisingProposalService fundraisingProposalService) {
    this.fundraisingProposalService = fundraisingProposalService;
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
}
