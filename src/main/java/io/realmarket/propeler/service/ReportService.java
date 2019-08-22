package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.FileDto;

public interface ReportService {

  FileDto generateUserWildcardsPdf(final Long authId);
}
