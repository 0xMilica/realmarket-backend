package io.realmarket.propeler.service;

import com.lowagie.text.DocumentException;
import io.realmarket.propeler.api.dto.FileDto;

import java.io.IOException;

public interface ReportService {

  FileDto generateUserWildcardsPdf(final Long authId) throws IOException, DocumentException;
}
