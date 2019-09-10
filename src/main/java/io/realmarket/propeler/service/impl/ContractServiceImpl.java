package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.ContractRequestDto;
import io.realmarket.propeler.api.dto.ContractResponseDto;
import io.realmarket.propeler.model.enums.FileType;
import io.realmarket.propeler.service.ContractService;
import io.realmarket.propeler.service.FileService;
import io.realmarket.propeler.service.util.PdfService;
import io.realmarket.propeler.service.util.TemplateDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@Slf4j
public class ContractServiceImpl implements ContractService {
  private static final String DUMMY_TYPE = "dummy";

  private final FileService fileService;
  private final PdfService pdfService;
  private final TemplateDataUtil templateDataUtil;

  @Autowired
  public ContractServiceImpl(PdfService pdfService, TemplateDataUtil templateDataUtil, FileService fileService) {
    this.pdfService = pdfService;
    this.templateDataUtil = templateDataUtil;
    this.fileService = fileService;
  }

  @Override
  public ContractResponseDto getContract(
      String contractType, ContractRequestDto contractRequestDto) {
    switch (contractType) {
      case DUMMY_TYPE:
        return getDummyContract(contractRequestDto);
      default:
        return null;
    }
  }

  private ContractResponseDto getDummyContract(ContractRequestDto contractRequestDto) {
    byte[] file = pdfService.generatePdf(templateDataUtil.getContractData(contractRequestDto), FileType.CONTRACT);
    String url = fileService.uploadFile(file, "pdf");
    return new ContractResponseDto(url, Base64.getEncoder().encodeToString(file));
  }
}
