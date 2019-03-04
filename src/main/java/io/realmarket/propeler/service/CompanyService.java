package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Company;

public interface CompanyService {

  Company save(Company company);

  Company findByIdOrThrowException(Long id);
}
