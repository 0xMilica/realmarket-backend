package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.ContractRequestDto;
import io.realmarket.propeler.api.dto.ContractResponseDto;

public interface ContractService {
  ContractResponseDto getContract(String contractType, ContractRequestDto contractRequestDto);
}
