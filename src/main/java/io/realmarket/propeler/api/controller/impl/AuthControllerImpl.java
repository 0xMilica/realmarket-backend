package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.AuthController;
import io.realmarket.propeler.api.dto.ConfirmRegistrationDto;
import io.realmarket.propeler.api.dto.LoginDto;
import io.realmarket.propeler.api.dto.RegistrationDto;
import io.realmarket.propeler.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

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

  @PostMapping()
  public ResponseEntity login(@RequestBody @Valid LoginDto loginDto) {
    authService.login(loginDto);
    return new ResponseEntity(CREATED);
  }
}
