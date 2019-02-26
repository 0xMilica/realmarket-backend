package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/auth")
public interface AuthController {

  @ApiOperation(
      value = "Register new user",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "User successfully created."),
    @ApiResponse(code = 400, message = "Invalid request."),
    @ApiResponse(code = 409, message = "User with the provided username already exists."),
    @ApiResponse(code = 500, message = "A problem with sending email message has occurred.")
  })
  ResponseEntity register(RegistrationDto registerDto);

  @ApiOperation(
      value = "Confirm registered user.",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "User successfully activated."),
    @ApiResponse(code = 400, message = "Invalid token provided.")
  })
  ResponseEntity confirmRegistration(ConfirmRegistrationDto confirmRegistrationDto);

  @ApiOperation(
      value = "Reset password.",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Reset password request created."),
    @ApiResponse(code = 400, message = "Username doesn't exist.")
  })
  ResponseEntity initializeResetPassword(@RequestBody @Valid UsernameDto usernameDto);

  @ApiOperation(
      value = "Finalize reset password",
      httpMethod = "PATCH",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 200, message = "Successfully reset password."),
    @ApiResponse(code = 400, message = "Invalid token provided.")
  })
  ResponseEntity finalizeResetPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto);

  @ApiOperation(
      value = "Login",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Created (if success)"),
    @ApiResponse(code = 400, message = "Invalid Request (on failure to login)\n")
  })
  ResponseEntity<AuthResponseDto> login(
      @RequestBody @Valid LoginDto loginDto, HttpServletRequest request);

  @ApiOperation(
      value = "Logout",
      httpMethod = "DELETE",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 204, message = "Successfully logged out"),
    @ApiResponse(code = 400, message = "Invalid attempt to logout\n")
  })
  ResponseEntity logout(HttpServletRequest request);

  @ApiOperation(
      value = "Recovering username",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Recover username request created."),
    @ApiResponse(code = 400, message = "Invalid username provided.")
  })
  ResponseEntity recoverUsername(EmailDto emailDto);

  @ApiOperation(
      value = "Changing email",
      httpMethod = "PATCH",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Changed email successfully."),
    @ApiResponse(code = 400, message = "Invalid token provided.")
  })
  ResponseEntity finalizeEmailChange(ConfirmEmailChangeDto confirmEmailChangeDto);
}
