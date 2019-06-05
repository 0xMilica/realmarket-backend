package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.UserController;
import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.DocumentService;
import io.realmarket.propeler.service.PersonService;
import io.realmarket.propeler.service.TwoFactorAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/users")
public class UserControllerImpl implements UserController {

  private final AuthService authService;
  private final PersonService personService;
  private final TwoFactorAuthService twoFactorAuthService;
  private final DocumentService documentService;

  public UserControllerImpl(
      AuthService authService,
      PersonService personService,
      TwoFactorAuthService twoFactorAuthService,
      DocumentService documentService) {
    this.authService = authService;
    this.personService = personService;
    this.twoFactorAuthService = twoFactorAuthService;
    this.documentService = documentService;
  }

  @RequestMapping(value = "{username}", method = RequestMethod.HEAD)
  public ResponseEntity<Void> userExists(@PathVariable String username) {
    authService.findByUsernameOrThrowException(username);
    return new ResponseEntity<>(OK);
  }

  @PostMapping(value = "/{authId}/password")
  public ResponseEntity initializeChangePassword(
      @PathVariable Long authId, @RequestBody @Valid ChangePasswordDto changePasswordDto) {
    authService.initializeChangePassword(authId, changePasswordDto);
    return new ResponseEntity<>(CREATED);
  }

  @PatchMapping(value = "/{authId}/password")
  public ResponseEntity finalizeChangePassword(
      @PathVariable Long authId, @RequestBody @Valid TwoFADto twoFADto) {
    authService.finalizeChangePassword(authId, twoFADto);
    return new ResponseEntity<>(OK);
  }

  @Override
  @PostMapping(value = "/{authId}/password/verify")
  public ResponseEntity<TokenDto> verifyPassword(
      @PathVariable Long authId, @RequestBody @Valid PasswordDto passwordDto) {
    return ResponseEntity.ok(authService.verifyPasswordAndReturnToken(authId, passwordDto));
  }

  @GetMapping(value = "/{userId}")
  public ResponseEntity<PersonDto> getPerson(@PathVariable Long userId) {
    return ResponseEntity.ok(personService.getPerson(userId));
  }

  @PostMapping("/{authId}/email")
  public ResponseEntity initializeEmailChange(
      @PathVariable Long authId, @RequestBody @Valid EmailDto emailDto) {
    authService.initializeEmailChange(authId, emailDto);
    return new ResponseEntity(CREATED);
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

  @GetMapping(value = "/{userId}/picture", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FileDto> getProfilePicture(@PathVariable Long userId) {
    return ResponseEntity.ok(personService.getProfilePicture(userId));
  }

  @DeleteMapping(value = "/{userId}/picture")
  public ResponseEntity deleteProfilePicture(@PathVariable Long userId) {
    personService.deleteProfilePicture(userId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping(value = "/{authId}/email")
  public ResponseEntity verifyEmailChangeRequest(
      @PathVariable Long authId, @RequestBody @Valid TwoFADto twoFADto) {
    authService.verifyEmailChangeRequest(authId, twoFADto);
    return ResponseEntity.ok().build();
  }

  @PostMapping(value = "/{userId}/secret")
  public ResponseEntity<SecretDto> generateNewSecret(
      @PathVariable Long userId, @RequestBody TwoFATokenDto twoFATokenDto) {
    return new ResponseEntity<>(
        twoFactorAuthService.generateNewSecret(twoFATokenDto, userId), CREATED);
  }

  @Override
  @PostMapping(value = "/{userId}/recovery_code")
  public ResponseEntity<OTPWildcardResponseDto> regenerateWildcards(
      @PathVariable Long userId, @RequestBody TwoFADto twoFADto) {
    return new ResponseEntity<>(twoFactorAuthService.createWildcards(userId, twoFADto), CREATED);
  }

  @PatchMapping(value = "/{userId}/secret")
  public ResponseEntity verifySecretChange(
      @PathVariable Long userId, @RequestBody VerifySecretChangeDto verifySecretChangeDto) {
    twoFactorAuthService.verifyNewSecret(verifySecretChangeDto, userId);
    return ResponseEntity.ok().build();
  }

  @Override
  @PreAuthorize("hasAnyAuthority('ROLE_ADMIN, ROLE_ENTREPRENEUR')")
  @GetMapping(value = "/{userId}/documents")
  public ResponseEntity<List<DocumentResponseDto>> getDocuments(@PathVariable Long userId) {
    return ResponseEntity.ok(documentService.getDocuments(userId));
  }
}
