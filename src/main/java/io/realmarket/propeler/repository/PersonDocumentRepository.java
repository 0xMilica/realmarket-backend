package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.model.PersonDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonDocumentRepository extends JpaRepository<PersonDocument, Long> {
  List<PersonDocument> findAllByPerson(Person person);
}
