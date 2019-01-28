package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.AuthController;
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
}
