package io.realmarket.propeler.service;

import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.api.dto.PersonPatchDto;
import io.realmarket.propeler.api.dto.PersonResponseDto;
import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Person;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PersonService {
  Person save(Person person);

  List<Person> findByEmail(String email);

  PersonResponseDto getPerson(Long userId);

  PersonResponseDto patchPerson(Long userId, PersonPatchDto personPatchDto);

  void uploadProfilePicture(Long personId, MultipartFile picture);

  FileDto getProfilePicture(Long personId);

  void deleteProfilePicture(Long personId);

  Person findByIdOrThrowException(Long id);

  Person getPersonFromAuth(Auth auth);
}
