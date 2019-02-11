package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.repository.PersonRepository;
import io.realmarket.propeler.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
