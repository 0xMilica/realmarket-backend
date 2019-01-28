package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.repository.PersonRepository;
import io.realmarket.propeler.service.impl.PersonServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.realmarket.propeler.unit.util.PersonUtils.TEST_PERSON;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PersonServiceImpl.class)
public class PersonServiceImplTest {
  @Mock private PersonRepository personRepository;

  @InjectMocks private PersonServiceImpl personServiceImpl;

  @Test
  public void Save_Should_SavePerson() {
    when(personRepository.save(TEST_PERSON)).thenReturn(TEST_PERSON);

    personServiceImpl.save(TEST_PERSON);

    verify(personRepository, Mockito.times(1)).save(TEST_PERSON);
  }
}
