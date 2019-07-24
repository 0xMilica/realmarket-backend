package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.PersonDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDocumentRepository extends JpaRepository<PersonDocument, Long> {}
