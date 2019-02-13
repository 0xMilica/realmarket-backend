package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.*;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

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
      value = "Password change.",
      httpMethod = "PATCH",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "userId",
        value = "id of person that password is about to change",
        required = true,
        dataType = "Long"),
    @ApiImplicitParam(
        name = "changePasswordDto",
        value = "Old/New password pair needed for changing password",
        dataType = "ChangePasswordDto")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "User changes password."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 500, message = "A problem with changing password has occurred.")
  })
  ResponseEntity changePassword(Long userId, ChangePasswordDto changePasswordDto);

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
  ResponseEntity createEmailChangeRequest(@PathVariable Long userId, EmailDto emailDto);

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
}
