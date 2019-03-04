package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {}
