package io.realmarket.propeler.service.util;

import com.lowagie.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
public class PdfService {

  @Value("${reports.template-names.user-wildcards-pdf}")
  private String userWildcardsTemplate;

  private final TemplateEngine templateEngine;

  @Autowired
  public PdfService(TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  public byte[] generateUserWildcardsPdf(Map<String, Object> parameters)
      throws IOException, DocumentException {
    return generatePdf(parameters, userWildcardsTemplate);
  }

  private byte[] generatePdf(final Map<String, Object> parameters, final String template)
      throws IOException, DocumentException {
    final Context ctx = new Context();
    parameters.entrySet().forEach(key -> ctx.setVariable(key.getKey(), key.getValue()));
    final String processedHtml = templateEngine.process(template, ctx);

    try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
      final ITextRenderer renderer = new ITextRenderer();
      renderer.setDocumentFromString(processedHtml);
      renderer.layout();
      renderer.createPDF(byteArrayOutputStream);
      renderer.finishPDF();

      return byteArrayOutputStream.toByteArray();
    }
  }
}
