package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.repository.CompanyRepository;
import io.realmarket.propeler.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class CompanyServiceImpl implements CompanyService {

  private final CompanyRepository companyRepository;

  @Autowired
  public CompanyServiceImpl(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
  }

  public Company save(Company company) {
    return companyRepository.save(company);
  }

  public Company findByIdOrThrowException(Long id) {
    return companyRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Company with provided id does not exist."));
  }
}
