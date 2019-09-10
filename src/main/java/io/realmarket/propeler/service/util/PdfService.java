package io.realmarket.propeler.service.util;

import io.realmarket.propeler.model.enums.FileType;
import io.realmarket.propeler.service.exception.BadRequestException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Slf4j
@Service
public class PdfService {

  private final TemplateEngine templateEngine;

  @Autowired
  public PdfService(TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  public byte[] generatePdf(final Map<String, Object> parameters, FileType fileType) {
    String template = getFileTemplate(fileType);

    final Context ctx = new Context();
    ctx.setVariables(parameters);

    final String processedHtml = templateEngine.process(template, ctx);

    try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
      final ITextRenderer renderer = new ITextRenderer();
      renderer.setDocumentFromString(processedHtml);
      renderer.layout();
      renderer.createPDF(byteArrayOutputStream);
      renderer.finishPDF();

      return byteArrayOutputStream.toByteArray();
    } catch (Exception e) {
      log.error("Problem with generating pdf file: " + e.getCause());
      throw new BadRequestException(ExceptionMessages.PDF_FILE_GENERATING_EXCEPTION);
    }
  }

  private String getFileTemplate(FileType fileType) {
    switch (fileType) {
      case WILD_CARDS:
        return "userWildcardsPdf";
      case PROFORMA_INVOICE:
      case INVOICE:
      case OFFPLATFORM_PROFORMA_INVOICE:
      case OFFPLATFORM_INVOICE:
        return "invoiceDocumentTemplate";
      case CONTRACT:
        return "contracts/contractDocumentTemplate.html";
      default:
        throw new BadRequestException(ExceptionMessages.INVALID_REQUEST);
    }
  }
}
