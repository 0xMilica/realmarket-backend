package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.api.dto.RegistrationDto;
import io.realmarket.propeler.model.Person;

import static io.realmarket.propeler.unit.util.AuthUtils.TEST_REGISTRATION_DTO;

public class PersonUtils {
  public static final Person TEST_PERSON = new Person(TEST_REGISTRATION_DTO);

  public static Person getMockedPerson(RegistrationDto registrationDto) {
    return new Person(registrationDto);
  }
}
