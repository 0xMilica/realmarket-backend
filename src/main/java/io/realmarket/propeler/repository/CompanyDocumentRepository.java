package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Company;
import io.realmarket.propeler.model.CompanyDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyDocumentRepository extends JpaRepository<CompanyDocument, Long> {

  List<CompanyDocument> findAllByCompany(Company company);
}
