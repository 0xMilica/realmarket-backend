package io.realmarket.propeler.security;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.JWT;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.JWTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Slf4j
public final class AuthorizationFilter extends GenericFilterBean {

  private static final String AUTH_HEADER_NAME = "Authorization";

  private final JWTService jwtService;
  private final AuthService authService;

  @Autowired
  public AuthorizationFilter(JWTService jwtService, AuthService authService) {
    this.jwtService = jwtService;
    this.authService = authService;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String requestToken = httpRequest.getHeader(AUTH_HEADER_NAME);

    if (requestToken != null) {
      JWT jwt = jwtService.findByValueAndNotExpiredOrThrowException(requestToken);

      Auth auth = authService.findByIdOrThrowException(jwt.getAuth().getId());

      UserAuthentication userAuth = new UserAuthentication(auth, jwt.getValue());
      SecurityContextHolder.getContext().setAuthentication(userAuth);
      jwtService.prolongExpirationTime(jwt);
    } else {
      log.info("NO REQUEST TOKEN");
      // TODO: Handle routes that don't need request token; throw exception only if token is required.

    }

    chain.doFilter(request, response);
  }
}
