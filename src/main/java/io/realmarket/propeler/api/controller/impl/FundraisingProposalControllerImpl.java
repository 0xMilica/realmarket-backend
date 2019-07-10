package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.annotations.RequireCaptcha;
import io.realmarket.propeler.api.controller.FundraisingProposalController;
import io.realmarket.propeler.api.dto.FundraisingProposalDto;
import io.realmarket.propeler.api.dto.FundraisingProposalResponseDto;
import io.realmarket.propeler.service.FundraisingProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
