package io.realmarket.propeler.api.controller;

import org.springframework.http.ResponseEntity;

public interface UserController {

  ResponseEntity<Void> userExists(String username);
}
