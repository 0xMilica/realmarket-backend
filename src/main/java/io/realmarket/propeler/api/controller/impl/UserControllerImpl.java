package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.UserController;
import io.realmarket.propeler.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserControllerImpl implements UserController {

  private final AuthService authService;

  public UserControllerImpl(AuthService authService) {
    this.authService = authService;
  }

  @RequestMapping(value = "{username}", method = RequestMethod.HEAD)
  public ResponseEntity<Void> userExists(@PathVariable String username) {
    authService.findByUsernameOrThrowException(username);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
