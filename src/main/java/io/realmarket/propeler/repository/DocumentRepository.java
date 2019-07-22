package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {}
