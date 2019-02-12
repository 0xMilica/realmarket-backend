package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.UserController;
import io.realmarket.propeler.api.dto.ChangePasswordDto;
import io.realmarket.propeler.api.dto.PersonDto;
import io.realmarket.propeler.api.dto.PersonPatchDto;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/users")
public class UserControllerImpl implements UserController {

  private final AuthService authService;
  private final PersonService personService;

  public UserControllerImpl(AuthService authService, PersonService personService) {
    this.authService = authService;
    this.personService = personService;
  }

  @RequestMapping(value = "{username}", method = RequestMethod.HEAD)
  public ResponseEntity<Void> userExists(@PathVariable String username) {
    authService.findByUsernameOrThrowException(username);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  @PatchMapping(value = "/{userId}/password")
  public ResponseEntity changePassword(
      @PathVariable Long userId, @RequestBody @Valid ChangePasswordDto changePasswordDto) {
    authService.changePassword(userId, changePasswordDto);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  @GetMapping(value = "/{userId}")
  public ResponseEntity<PersonDto> getPerson(@PathVariable Long userId) {
    return ResponseEntity.ok(personService.getPerson(userId));
  }

  @Override
  @PatchMapping(value = "/{userId}")
  public ResponseEntity<PersonDto> patchPerson(
      @PathVariable Long userId, @RequestBody PersonPatchDto personPatchDto) {
    return ResponseEntity.ok(personService.patchPerson(userId, personPatchDto));
  }

  @PostMapping(
      value = "/{userId}/picture",
      consumes = "multipart/form-data",
      produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity uploadProfilePicture(
      @PathVariable Long userId, @RequestParam("picture") MultipartFile picture) {
    personService.uploadProfilePicture(userId, picture);
    return new ResponseEntity<>(HttpStatus.CREATED);

  }
}
