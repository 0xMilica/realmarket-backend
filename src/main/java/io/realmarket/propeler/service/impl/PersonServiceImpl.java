package io.realmarket.propeler.service.impl;

import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.api.dto.PersonDto;
import io.realmarket.propeler.api.dto.PersonPatchDto;
import io.realmarket.propeler.model.Person;
import io.realmarket.propeler.repository.PersonRepository;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.PersonService;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.FileUtils;
import io.realmarket.propeler.service.util.ModelMapperBlankString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Slf4j
public class PersonServiceImpl implements PersonService {

  private final PersonRepository personRepository;
  private final ModelMapperBlankString modelMapperBlankString;
  private final CloudObjectStorageService cloudObjectStorageService;

  @Value(value = "${cos.file_prefix.user_picture}")
  private String userPicturePrefix;

  @Autowired
  public PersonServiceImpl(
      PersonRepository personRepository,
      ModelMapperBlankString modelMapperBlankString,
      CloudObjectStorageService cloudObjectStorageService) {
    this.personRepository = personRepository;
    this.modelMapperBlankString = modelMapperBlankString;
    this.cloudObjectStorageService = cloudObjectStorageService;
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

  @Transactional
  public PersonDto patchPerson(Long id, PersonPatchDto personPatchDto) {
    Person person = findByIdOrThrowException(id);
    modelMapperBlankString.map(personPatchDto, person);
    return new PersonDto(personRepository.save(person));
  }

  @Override
  @Transactional
  public void uploadProfilePicture(Long id, MultipartFile picture) {
    log.info("Picture upload requested");
    String extension = FileUtils.getExtensionOrThrowException(picture);
    Person person = findByIdOrThrowException(id);
    String url = String.join("", userPicturePrefix, person.getAuth().getUsername(), ".", extension);
    cloudObjectStorageService.uploadAndReplace(person.getProfilePictureUrl(), url, picture);
    person.setProfilePictureUrl(url);

    personRepository.save(person);
  }

  public FileDto getProfilePicture(Long personId) {
    return cloudObjectStorageService.downloadFileDto(
        findByIdOrThrowException(personId).getProfilePictureUrl());
  }

  public void deleteProfilePicture(Long personId) {
    Person person = findByIdOrThrowException(personId);
    String pictureUrl = person.getProfilePictureUrl();

    if (StringUtils.isEmpty(pictureUrl)) {
      throw new EntityNotFoundException(ExceptionMessages.PROFILE_PICTURE_DOES_NOT_EXIST);
    }

    cloudObjectStorageService.delete(pictureUrl);

    person.setProfilePictureUrl(null);
    save(person);
  }
}
