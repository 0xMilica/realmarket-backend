package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.*;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/users")
public interface UserController {

  @ApiOperation(value = "Check if the user with provided username exists.", httpMethod = "HEAD")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "username", value = "Username that is provided for existence checking")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "User with username already exists"),
    @ApiResponse(code = 404, message = "Username is available"),
  })
  ResponseEntity<Void> userExists(String username);

  @ApiOperation(
      value = "Password change initialization",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "authId",
        value = "id of auth for person for whom the password is about to change",
        required = true,
        dataType = "Long"),
    @ApiImplicitParam(
        name = "changePasswordDto",
        value = "Old/New password pair needed for changing password",
        dataType = "ChangePasswordDto")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Change password request created."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 500, message = "A problem with changing password has occurred.")
  })
  ResponseEntity initializeChangePassword(Long authId, ChangePasswordDto changePasswordDto);

  @ApiOperation(
      value = "Password change finalization",
      httpMethod = "PATCH",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "authId",
        value = "id of auth for person for whom the password is about to change",
        required = true,
        dataType = "Long"),
    @ApiImplicitParam(
        name = "twoFACodeDto",
        value = "Two-factor authentication code dto",
        dataType = "TwoFACodeDto")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Change password request created."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 500, message = "A problem with changing password has occurred.")
  })
  ResponseEntity finalizeChangePassword(
      @PathVariable Long authId, @RequestBody @Valid TwoFADto twoFADto);

  @ApiOperation(
      value = "Verify password and return token",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "authId",
        value = "id of auth for person for whom want to verify its password",
        required = true,
        dataType = "Long"),
    @ApiImplicitParam(
        name = "PasswordDto",
        value = "Password that we want to verify.",
        dataType = "TwoFACodeDto")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Password verified"),
    @ApiResponse(code = 400, message = "Invalid request."),
  })
  ResponseEntity<TokenDto> verifyPassword(
      @PathVariable Long authId, @RequestBody @Valid PasswordDto passwordDto);

  @ApiOperation(
      value = "Get person profile",
      httpMethod = "GET",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "userId",
        value = "id of person that profile we want get",
        required = true,
        dataType = "Long"),
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Returned user profile."),
    @ApiResponse(code = 400, message = "Invalid request."),
  })
  ResponseEntity<PersonDto> getPerson(Long userId);

  @ApiOperation(
      value = "Change email",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Change email request created."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity initializeEmailChange(@PathVariable Long userId, EmailDto emailDto);

  @ApiOperation(
      value = "Patch person arguments",
      httpMethod = "PATCH",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "userId",
        value = "Identifier of person that profile we want patch",
        required = true,
        dataType = "Long")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "Return person profile."),
    @ApiResponse(code = 400, message = "Invalid request."),
  })
  ResponseEntity<PersonDto> patchPerson(Long userId, PersonPatchDto personPatchDto);

  @ApiOperation(
      value = "Upload profile picture",
      httpMethod = "POST",
      consumes = "multipart/form-data",
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams(
      @ApiImplicitParam(
          name = "profile picture",
          dataType = "file",
          value = "Picture to be uploaded",
          paramType = "form",
          required = true))
  @ApiResponses({
    @ApiResponse(code = 200, message = "Picture successfully uploaded."),
    @ApiResponse(code = 400, message = "Picture cannot be saved.")
  })
  ResponseEntity uploadProfilePicture(Long userId, MultipartFile picture);

  @ApiOperation(
      value = "Get profile picture",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Picture retrieved successfully."),
    @ApiResponse(code = 404, message = "Picture not found.")
  })
  ResponseEntity<FileDto> getProfilePicture(@PathVariable Long userId);

  @ApiOperation(
      value = "Get profile picture",
      httpMethod = "DELETE",
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 204, message = "Picture deleted successfully."),
    @ApiResponse(code = 404, message = "Picture not found.")
  })
  ResponseEntity deleteProfilePicture(@PathVariable Long userId);

  @ApiOperation(
      value = "Change email request verification",
      httpMethod = "PATCH",
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Email change request verified."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity verifyEmailChangeRequest(
      @PathVariable Long userId, @RequestBody TwoFADto twoFACDto);

  @ApiOperation(
      value = "Generate new secret codes",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams(
      @ApiImplicitParam(
          name = "userId",
          value = "id of person that secret is about to change",
          required = true,
          dataType = "Long"))
  @ApiResponses({
    @ApiResponse(code = 201, message = "Created."),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity<SecretDto> generateNewSecret(Long userId, TwoFATokenDto twoFATokenDto);

  @ApiOperation(
      value = "Regenerate wildcard codes",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParam(
      name = "userId",
      value = "id of person that wildcards will be regenerated",
      required = true,
      dataType = "Long")
  @ApiResponses({
    @ApiResponse(code = 201, message = "Regenerated new wildcard codes."),
    @ApiResponse(code = 404, message = "Person not found.")
  })
  ResponseEntity<OTPWildcardResponseDto> regenerateWildcards(Long userId, TwoFADto twoFADto);

  @ApiOperation(
      value = "Verify newly generated secret",
      httpMethod = "PATCH",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams(
      @ApiImplicitParam(
          name = "userId",
          value = "id of person that secret is verifying",
          required = true,
          dataType = "Long"))
  @ApiResponses({
    @ApiResponse(code = 200, message = "OK"),
    @ApiResponse(code = 400, message = "Invalid request.")
  })
  ResponseEntity verifySecretChange(Long userId, VerifySecretChangeDto verifySecretChangeDto);

  @ApiOperation(
      value = "Get documents submitted by user",
      httpMethod = "GET",
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "User documents successfully retrieved."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 401, message = "Unauthorized attempt to retrieve campaign documents."),
    @ApiResponse(code = 404, message = "User not found.")
  })
  ResponseEntity<List<DocumentResponseDto>> getDocuments(Long userId);
}
