package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.ChangePasswordDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/users")
public interface UserController {

  ResponseEntity<Void> userExists(String username);

  @ApiOperation(
      value = "/{userId}/password",
      httpMethod = "PATCH",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "userId",
        value =
            "id of person that password is about to change",
        dataType = "long"),
    @ApiImplicitParam(
        name = "changePasswordDto",
        value =
            "Old/New password pair needed for changing password",
        dataType = "ChangePasswordDto")
  })
  @ApiResponses({
    @ApiResponse(code = 200, message = "User changes password."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 500, message = "A problem with changing password has occurred.")
  })
  ResponseEntity changePassword(Long userId, ChangePasswordDto changePasswordDto);
}
