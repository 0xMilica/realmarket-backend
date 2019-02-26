package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.AuthController;
import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = "/auth")
@Slf4j
public class AuthControllerImpl implements AuthController {

  private final AuthService authService;

  @Autowired
  public AuthControllerImpl(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping(value = "/register")
  public ResponseEntity register(@RequestBody @Valid RegistrationDto registrationDto) {
    authService.register(registrationDto);
    return new ResponseEntity(CREATED);
  }

  @PostMapping(value = "/confirm_registration")
  public ResponseEntity confirmRegistration(
      @RequestBody @Valid ConfirmRegistrationDto confirmRegistrationDto) {
    authService.confirmRegistration(confirmRegistrationDto);
    return new ResponseEntity(OK);
  }

  @PostMapping(value = "/reset_password")
  public ResponseEntity initializeResetPassword(@RequestBody @Valid UsernameDto usernameDto) {
    authService.initializeResetPassword(usernameDto);
    return new ResponseEntity(CREATED);
  }

  @PatchMapping(value = "/reset_password")
  public ResponseEntity finalizeResetPassword(
      @RequestBody @Valid ResetPasswordDto resetPasswordDto) {
    authService.finalizeResetPassword(resetPasswordDto);
    return new ResponseEntity(OK);
  }

  @PostMapping()
  public ResponseEntity<AuthResponseDto> login(
      @RequestBody @Valid LoginDto loginDto, HttpServletRequest request) {
    return new ResponseEntity<>(authService.login(loginDto, request), CREATED);
  }

  @PostMapping("/recover_username")
  public ResponseEntity recoverUsername(@RequestBody @Valid EmailDto emailDto) {
    authService.recoverUsername(emailDto);
    return new ResponseEntity(CREATED);
  }

  @PatchMapping("/email_confirm")
  public ResponseEntity finalizeEmailChange(
      @RequestBody @Valid ConfirmEmailChangeDto confirmEmailChangeDto) {
    authService.finalizeEmailChange(confirmEmailChangeDto);
    return new ResponseEntity(OK);
  }

  @DeleteMapping()
  public ResponseEntity logout(HttpServletRequest request) {
    authService.logout(request);
    return new ResponseEntity(NO_CONTENT);
  }
}
