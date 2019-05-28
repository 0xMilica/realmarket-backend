package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.api.dto.PersonDto;
import io.realmarket.propeler.api.dto.PersonPatchDto;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.repository.PersonRepository;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import io.realmarket.propeler.util.AuthUtils;
import io.realmarket.propeler.util.FileUtils;
import io.realmarket.propeler.util.PersonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static io.realmarket.propeler.util.AuthUtils.TEST_AUTH_ID;
import static io.realmarket.propeler.util.FileUtils.TEST_FILE_NAME_2;
import static io.realmarket.propeler.util.PersonUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PersonServiceImpl.class)
@TestPropertySource(
    properties = {
      "cos.file_prefix.user_picture=user_picture/",
    })
public class PersonServiceImplTest {
  @Mock private PersonRepository personRepository;
  @Mock private ModelMapperBlankString modelMapperBlankString;
  @Mock private CloudObjectStorageService cloudObjectStorageService;

  @Value(value = "${cos.file_prefix.user_picture}")
  private String userPicturePrefix;

  @InjectMocks private PersonServiceImpl personServiceImpl;

  @Test
  public void Save_Should_SavePerson() {
    when(personRepository.save(TEST_REGISTRATION_PERSON)).thenReturn(TEST_REGISTRATION_PERSON);

    personServiceImpl.save(TEST_REGISTRATION_PERSON);

    verify(personRepository, Mockito.times(1)).save(TEST_REGISTRATION_PERSON);
  }

  @Test
  public void PatchPerson_Should_CallModelMapper() {
    Person testPerson = TEST_PERSON.toBuilder().build();
    PersonPatchDto personPatchDto = PersonUtils.TEST_PERSON_PATCH_DTO_LAST_NAME();
    when(personRepository.findById(TEST_AUTH_ID)).thenReturn(Optional.of(testPerson));
    when(personRepository.save(testPerson)).thenReturn(testPerson);
    doAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              ((Person) args[1]).setLastName(PersonUtils.TEST_PERSON_LAST_NAME);
              return null;
            })
        .when(modelMapperBlankString)
        .map(personPatchDto, testPerson);

    PersonDto personDto = personServiceImpl.patchPerson(TEST_AUTH_ID, personPatchDto);
    assertEquals(PersonUtils.TEST_PERSON_LAST_NAME, personDto.getLastName());
  }

  @Test
  public void UploadFile_Should_SaveToRepository() {
    Person testPerson = TEST_PERSON.toBuilder().build();
    testPerson.setProfilePictureUrl(null);
    when(personRepository.findById(TEST_AUTH_ID)).thenReturn(Optional.of(testPerson));
    personServiceImpl.uploadProfilePicture(TEST_AUTH_ID, FileUtils.MOCK_FILE_VALID);

    // verify(cloudObjectStorageService, times(0)).delete(any());
    verify(cloudObjectStorageService, times(1))
        .uploadAndReplace(
            null,
            String.join(
                "", userPicturePrefix, AuthUtils.TEST_USERNAME, ".", FileUtils.TEST_FILE_TYPE),
            FileUtils.MOCK_FILE_VALID);
    verify(personRepository, times(1)).save(testPerson);
  }

  @Test
  public void UploadFile_Should_DeleteOldPicture_And_SaveToRepository() {
    Person testPerson = TEST_PERSON.toBuilder().build();
    testPerson.setProfilePictureUrl(TEST_FILE_NAME_2);
    when(personRepository.findById(TEST_AUTH_ID)).thenReturn(Optional.of(testPerson));

    personServiceImpl.uploadProfilePicture(TEST_AUTH_ID, FileUtils.MOCK_FILE_VALID);

    verify(cloudObjectStorageService, times(1))
        .uploadAndReplace(
            TEST_FILE_NAME_2, testPerson.getProfilePictureUrl(), FileUtils.MOCK_FILE_VALID);
    verify(personRepository, times(1)).save(testPerson);
  }

  @Test(expected = IllegalArgumentException.class)
  public void UploadFile_Should_ThrowException_OnEmptyFile() {
    personServiceImpl.uploadProfilePicture(TEST_AUTH_ID, FileUtils.MOCK_FILE_EMPTY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void UploadFile_Should_ThrowException_NoExtension() {
    personServiceImpl.uploadProfilePicture(TEST_AUTH_ID, FileUtils.MOCK_FILE_NO_EXTENSION);
  }

  /*
  @Test(expected = StorageException.class)
  public void UploadFile_Should_Throw_StorageException() {
    when(fileService.uploadFile(TestUtils.MOCK_FILE_VALID)).thenThrow(StorageException.class);

    ResponseEntity<String> result = fileController.uploadFile(TestUtils.MOCK_FILE_VALID);

    verify(fileService, times(1)).uploadFile(TestUtils.MOCK_FILE_VALID);
    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void UploadFile_Should_Throw_IllegalArgumentException() {
    ResponseEntity<String> result = fileController.uploadFile(TestUtils.MOCK_FILE_EMPTY);

    verify(fileService, times(0)).uploadFile(TestUtils.MOCK_FILE_EMPTY);
    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }*/

  @Test
  public void GetProfilePicture_Should_ReturnProfilePicture() {
    when(personRepository.findById(TEST_PERSON_ID))
        .thenReturn(Optional.of(TEST_PERSON.toBuilder().build()));
    when(cloudObjectStorageService.downloadFileDto(TEST_PROFILE_PICTURE_URL))
        .thenReturn(FileUtils.TEST_FILE_DTO);

    FileDto returnFileDto = personServiceImpl.getProfilePicture(TEST_PERSON_ID);

    assertEquals(FileUtils.TEST_FILE_DTO, returnFileDto);
    verify(personRepository, Mockito.times(1)).findById(TEST_PERSON_ID);
    verify(cloudObjectStorageService, Mockito.times(1)).downloadFileDto(TEST_PROFILE_PICTURE_URL);
  }

  @Test(expected = EntityNotFoundException.class)
  public void GetProfilePicture_Should_Throw_EntityNotFoundException() {
    when(personRepository.findById(TEST_PERSON_ID))
        .thenReturn(Optional.of(TEST_PERSON_NO_PROFILE_PICTURE));
    doThrow(EntityNotFoundException.class).when(cloudObjectStorageService).downloadFileDto(any());

    personServiceImpl.getProfilePicture(TEST_PERSON_ID);
  }

  @Test
  public void DeleteProfilePicture_Should_DeleteProfilePicture() {
    when(personRepository.findById(TEST_PERSON_ID))
        .thenReturn(Optional.of(TEST_PERSON.toBuilder().build()));
    doNothing().when(cloudObjectStorageService).delete(TEST_PROFILE_PICTURE_URL);

    personServiceImpl.deleteProfilePicture(TEST_PERSON_ID);

    verify(personRepository, Mockito.times(1)).findById(TEST_PERSON_ID);
    verify(cloudObjectStorageService, Mockito.times(1)).delete(TEST_PROFILE_PICTURE_URL);
  }

  @Test(expected = EntityNotFoundException.class)
  public void DeleteProfilePicture_Should_Throw_EntityNotFoundException() {
    when(personRepository.findById(TEST_PERSON_ID))
        .thenReturn(Optional.of(TEST_PERSON_NO_PROFILE_PICTURE));

    personServiceImpl.deleteProfilePicture(TEST_PERSON_ID);
  }
}
