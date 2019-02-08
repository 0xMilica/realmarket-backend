package io.realmarket.propeler.unit.util;

import io.realmarket.propeler.api.dto.PersonPatchDto;
import io.realmarket.propeler.api.dto.RegistrationDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Person;

import java.util.Arrays;
import java.util.List;

import static io.realmarket.propeler.unit.util.AuthUtils.*;

public class PersonUtils {

  public static final Person TEST_REGISTRATION_PERSON = new Person(TEST_REGISTRATION_DTO);

  public static final Person TEST_PERSON =
      Person.builder()
          .email(TEST_EMAIL)
          .auth(Auth.builder().username(TEST_USERNAME).build())
          .build();

  public static final List<Person> TEST_PERSON_LIST =
      Arrays.asList(TEST_PERSON, TEST_PERSON, TEST_PERSON);

  public static final String TEST_PERSON_LAST_NAME = "LAST_NAME";

  public static PersonPatchDto TEST_PERSON_PATCH_DTO_LAST_NAME() {
    return PersonPatchDto.builder().firstName(TEST_PERSON_LAST_NAME).build();
  }

  public static Person getMockedPerson(RegistrationDto registrationDto) {
    return new Person(registrationDto);
  }
}
