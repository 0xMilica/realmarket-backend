package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.UserController;
import io.realmarket.propeler.api.dto.ChangePasswordDto;
import io.realmarket.propeler.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

  @Override
  @PatchMapping(value = "/{userId}/password")
  public ResponseEntity changePassword(
      @PathVariable Long userId, @RequestBody @Valid ChangePasswordDto changePasswordDto) {
    authService.changePassword(userId, changePasswordDto);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
