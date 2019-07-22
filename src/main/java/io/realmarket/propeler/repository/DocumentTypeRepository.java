package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.DocumentType;
import io.realmarket.propeler.model.enums.DocumentTypeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
  Optional<DocumentType> findByName(DocumentTypeName name);
}
