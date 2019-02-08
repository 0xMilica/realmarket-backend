package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.api.dto.PersonDto;
import io.realmarket.propeler.api.dto.PersonPatchDto;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.repository.PersonRepository;
import io.realmarket.propeler.service.impl.PersonServiceImpl;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import io.realmarket.propeler.unit.util.AuthUtils;
import io.realmarket.propeler.unit.util.PersonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static io.realmarket.propeler.unit.util.PersonUtils.TEST_REGISTRATION_PERSON;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PersonServiceImpl.class)
public class PersonServiceImplTest {
  @Mock private PersonRepository personRepository;
  @Mock private ModelMapperBlankString modelMapperBlankString;

  @InjectMocks private PersonServiceImpl personServiceImpl;

  @Test
  public void Save_Should_SavePerson() {
    when(personRepository.save(TEST_REGISTRATION_PERSON)).thenReturn(TEST_REGISTRATION_PERSON);

    personServiceImpl.save(TEST_REGISTRATION_PERSON);

    verify(personRepository, Mockito.times(1)).save(TEST_REGISTRATION_PERSON);
  }

  @Test
  public void PatchPerson_Should_CallModelMapper() {
    Person testPerson = PersonUtils.TEST_PERSON.toBuilder().build();
    PersonPatchDto personPatchDto = PersonUtils.TEST_PERSON_PATCH_DTO_LAST_NAME();
    when(personRepository.findById(AuthUtils.TEST_AUTH_ID)).thenReturn(Optional.of(testPerson));
    when(personRepository.save(testPerson)).thenReturn(testPerson);
    doAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              ((Person) args[1]).setLastName(PersonUtils.TEST_PERSON_LAST_NAME);
              return null;
            })
        .when(modelMapperBlankString)
        .map(personPatchDto, testPerson);

    PersonDto personDto = personServiceImpl.patchPerson(AuthUtils.TEST_AUTH_ID, personPatchDto);
    assertEquals(PersonUtils.TEST_PERSON_LAST_NAME, personDto.getLastName());
  }
}
