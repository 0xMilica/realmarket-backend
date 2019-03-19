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
  public static final String TEST_PROFILE_PICTURE_URL = "TEST_PROFILE_PICTURE_URL.EXT";
  public static final Long TEST_PERSON_ID = 1L;
  public static final byte[] TEST_PROFILE_PICTURE = "TEST_PROFILE_PICTURE".getBytes();
  public static final String TEST_NEW_EMAIL = "TEST_NEW_EMAIL";

  public static final Person TEST_PERSON =
      Person.builder()
          .id(TEST_PERSON_ID)
          .email(TEST_EMAIL)
          .profilePictureUrl(TEST_PROFILE_PICTURE_URL)
          .auth(Auth.builder().username(TEST_USERNAME).build())
          .build();

  public static final Person TEST_PERSON_NO_PROFILE_PICTURE =
      Person.builder()
          .id(TEST_PERSON_ID)
          .email(TEST_EMAIL)
          .profilePictureUrl(null)
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
