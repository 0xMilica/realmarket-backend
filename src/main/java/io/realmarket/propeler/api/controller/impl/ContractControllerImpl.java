package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.ContractController;
import io.realmarket.propeler.api.dto.ContractRequestDto;
import io.realmarket.propeler.api.dto.ContractResponseDto;
import io.realmarket.propeler.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/contracts")
public class ContractControllerImpl implements ContractController {
  private final ContractService contractService;

  @Autowired
  public ContractControllerImpl(ContractService contractService) {
    this.contractService = contractService;
  }

  @Override
  @GetMapping("/{contractType}")
  public ResponseEntity<ContractResponseDto> getContract(
      @PathVariable String contractType, ContractRequestDto contractRequestDto) {
    return ResponseEntity.ok(contractService.getContract(contractType, contractRequestDto));
  }
}
