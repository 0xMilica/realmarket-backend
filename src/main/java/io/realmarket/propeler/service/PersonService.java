package io.realmarket.propeler.service;

import io.realmarket.propeler.model.Person;

import java.util.List;

public interface PersonService {
  Person save(Person person);

  List<Person> findByEmail(String email);
}
