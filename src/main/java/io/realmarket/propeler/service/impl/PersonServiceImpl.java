package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.PersonDto;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.repository.PersonRepository;
import io.realmarket.propeler.service.PersonService;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

  private final PersonRepository personRepository;

  @Autowired
  public PersonServiceImpl(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public Person save(Person person) {
    return personRepository.save(person);
  }

  public List<Person> findByEmail(String email) {
    return personRepository.findByEmail(email);
  }

  public Person findByIdOrThrowException(Long id) {
    return personRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException(ExceptionMessages.PERSON_ID_DOES_NOT_EXISTS));
  }

  public PersonDto getPerson(Long id) {
    return new PersonDto(findByIdOrThrowException(id));
  }
}
