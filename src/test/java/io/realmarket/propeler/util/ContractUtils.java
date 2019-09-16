package io.realmarket.propeler.util;

import io.realmarket.propeler.api.dto.ContractRequestDto;

import java.util.Base64;
import java.util.Collections;

public class ContractUtils {
  public static final byte[] TEST_FILE = "\"Hello borld!\", says dummy contract.".getBytes();
  public static final String TEST_FILE_BASE_64 = Base64.getEncoder().encodeToString(TEST_FILE);
  public static final String TEST_FILE_URL = "https://contract.url/dummy";

  public static ContractRequestDto getContractRequestDtoMocked() {
    return ContractRequestDto.builder().signers(Collections.singletonList(1L)).build();
  }
}
