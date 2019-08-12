package io.realmarket.propeler.api.controller.impl;

import io.realmarket.propeler.api.controller.DigitalSignatureController;
import io.realmarket.propeler.api.dto.DigitalSignaturePrivateDto;
import io.realmarket.propeler.api.dto.DigitalSignaturePublicDto;
import io.realmarket.propeler.service.DigitalSignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/digitalSignatures")
public class DigitalSignatureControllerImpl implements DigitalSignatureController {

  private final DigitalSignatureService digitalSignatureService;

  @Autowired
  public DigitalSignatureControllerImpl(DigitalSignatureService digitalSignatureService) {
    this.digitalSignatureService = digitalSignatureService;
  }

  @GetMapping(value = "/mine")
  public ResponseEntity<DigitalSignaturePrivateDto> getMyDigitalSignature() {
    return new ResponseEntity<>(
        digitalSignatureService.getPrivateDigitalSignature(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity saveDigitalSignature(
      @RequestBody DigitalSignaturePrivateDto digitalSignaturePrivateDto) {
    digitalSignatureService.save(digitalSignaturePrivateDto);
    return new ResponseEntity(HttpStatus.CREATED);
  }

  @GetMapping(value = "/{authId}")
  public ResponseEntity<DigitalSignaturePublicDto> getDigitalSignature(@PathVariable Long authId) {
    return new ResponseEntity<>(
        digitalSignatureService.getPublicDigitalSignature(authId), HttpStatus.OK);
  }
}
