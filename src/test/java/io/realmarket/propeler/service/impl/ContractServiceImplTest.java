package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.ContractResponseDto;
import io.realmarket.propeler.model.enums.FileType;
import io.realmarket.propeler.service.FileService;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.util.PdfService;
import io.realmarket.propeler.service.util.TemplateDataUtil;
import io.realmarket.propeler.util.ContractUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static io.realmarket.propeler.util.ContractUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ContractServiceImpl.class)
public class ContractServiceImplTest {
  @InjectMocks
  ContractServiceImpl contractService;
  @Mock
  private FileService fileService;
  @Mock
  private PdfService pdfService;
  @Mock
  private TemplateDataUtil templateDataUtil;

  @Test
  public void getContract_Dummy_should_createAndReturn() {
    when(pdfService.generatePdf(any(Map.class), eq(FileType.CONTRACT)))
        .thenReturn(ContractUtils.TEST_FILE);
    when(fileService.uploadFile(any(byte[].class), any(String.class))).thenReturn(TEST_FILE_URL);
    ContractResponseDto contractResponseDto =
        contractService.getContract(contractService.DUMMY_TYPE, getContractRequestDtoMocked());
    assertEquals(contractResponseDto.getUrl(), TEST_FILE_URL);
    assertEquals(contractResponseDto.getContract(), TEST_FILE_BASE_64);
  }

  @Test(expected = BadRequestException.class)
  public void getContract_Dummy_Should_Throw_BadRequestException() {
    contractService.getContract("keywithlength15", getContractRequestDtoMocked());
  }
}
