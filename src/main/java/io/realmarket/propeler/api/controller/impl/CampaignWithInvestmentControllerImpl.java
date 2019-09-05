package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.CampaignWithInvestmentController;
import io.realmarket.propeler.api.dto.CampaignWithInvestmentsWithPersonResponseDto;
import io.realmarket.propeler.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/campaignsWithInvestments")
public class CampaignWithInvestmentControllerImpl implements CampaignWithInvestmentController {

  private final CampaignService campaignService;

  @Autowired
  public CampaignWithInvestmentControllerImpl(CampaignService campaignService) {
    this.campaignService = campaignService;
  }

  @Override
  @GetMapping()
  @PreAuthorize("hasAnyAuthority('ROLE_ENTREPRENEUR', 'ROLE_ADMIN')")
  public ResponseEntity<Page<CampaignWithInvestmentsWithPersonResponseDto>>
      getCampaignsByStateWithInvestments(
          Pageable pageable, @RequestParam(value = "state", defaultValue = "active") String state) {
    return ResponseEntity.ok(campaignService.getCampaignsByStateWithInvestments(pageable, state));
  }
}
