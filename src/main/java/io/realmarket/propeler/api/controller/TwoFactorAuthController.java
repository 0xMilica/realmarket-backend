package io.realmarket.propeler.api.controller;

import io.realmarket.propeler.api.dto.*;
import io.realmarket.propeler.service.util.dto.LoginResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "/auth/2fa")
public interface TwoFactorAuthController {

  @ApiOperation(
      value = "",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Pass 2fa for login"),
    @ApiResponse(code = 400, message = "Invalid token or code provided.")
  })
  ResponseEntity<LoginResponseDto> login2FA(
      LoginTwoFADto loginTwoFADto, HttpServletResponse response);

  @ApiOperation(
      value = "",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "Remember me for login"),
    @ApiResponse(code = 400, message = "Invalid token or code provided.")
  })
  ResponseEntity<LoginResponseDto> loginRememberMe(
      LoginTwoFADto loginTwoFADto, HttpServletRequest request);

  @ApiOperation(
      value = "Create secret",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "User created new secret."),
    @ApiResponse(code = 400, message = "Invalid token provided.")
  })
  ResponseEntity<TwoFASecretResponseDto> createSecret(TwoFASecretRequestDto twoFASecretRequestDto);

  @ApiOperation(
      value = "Verify secret and create wildcard codes",
      httpMethod = "POST",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ApiResponses({
    @ApiResponse(code = 201, message = "User successfully verified secret and codes are returned."),
    @ApiResponse(code = 400, message = "Invalid code or token provided")
  })
  ResponseEntity<OTPWildcardResponseDto> verifySecretAndCreateWildcards(
      TwoFASecretVerifyDto twoFATokenDto);
}
