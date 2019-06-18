package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.CompanyDocumentType;
import io.realmarket.propeler.model.enums.ECompanyDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyDocumentTypeRepository extends JpaRepository<CompanyDocumentType, Long> {

  Optional<CompanyDocumentType> findByName(ECompanyDocumentType name);
}
