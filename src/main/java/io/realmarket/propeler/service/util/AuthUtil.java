package io.realmarket.propeler.service.util;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.JWT;
import io.realmarket.propeler.security.UserAuthentication;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.JWTService;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class AuthUtil {

  private static final String AUTH_HEADER_NAME = "Authorization";
  private static final String AUTH_SCHEME = "Bearer";

  private final JWTService jwtService;
  private final AuthService authService;
  private final HttpServletRequest httpRequest;

  @Autowired
  public AuthUtil(JWTService jwtService, AuthService authService, HttpServletRequest httpRequest) {
    this.jwtService = jwtService;
    this.authService = authService;
    this.httpRequest = httpRequest;
  }

  public void setAuthentication() {
    log.info("Request - {} {}", httpRequest.getMethod(), httpRequest.getRequestURL().toString());

    try {
      final String authHeader = httpRequest.getHeader(AUTH_HEADER_NAME);
      final String jwtToken = extractJwtFromAuthorizationHeader(authHeader);

      log.info("Jwt from request is {}", jwtToken);

      JWT jwt = jwtService.validateJWTOrThrowException(jwtToken);
      Auth auth = authService.findByIdOrThrowException(jwt.getAuth().getId());
      UserAuthentication userAuth = new UserAuthentication(auth, jwt.getValue());
      SecurityContextHolder.getContext().setAuthentication(userAuth);
      jwtService.prolongExpirationTime(jwt);

    } catch (Exception ex) {
      log.info(ExceptionMessages.INVALID_TOKEN_PROVIDED);
    }
  }

  private String extractJwtFromAuthorizationHeader(final String authHeader) {
    final String[] authHeaderParts = authHeader.split(" ");
    return AUTH_SCHEME.equals(authHeaderParts[0]) ? authHeaderParts[1] : null;
  }
}
