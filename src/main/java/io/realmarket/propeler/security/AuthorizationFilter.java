package io.realmarket.propeler.security;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Token;
import io.realmarket.propeler.service.AuthService;
import io.realmarket.propeler.service.TokenService;
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
import java.util.Optional;

@Component
public final class AuthorizationFilter extends GenericFilterBean {

  private static final String AUTH_HEADER_NAME = "Authorization";

  private final TokenService tokenService;
  private final AuthService authService;

  @Autowired
  public AuthorizationFilter(TokenService tokenService, AuthService authService) {
    this.tokenService = tokenService;
    this.authService = authService;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String requestToken = httpRequest.getHeader(AUTH_HEADER_NAME);

    if (requestToken != null) {
      Token authToken = tokenService.findByJwtOrThrowException(requestToken);

      if (authToken != null) {
        Optional<Auth> auth = authService.findById(authToken.getAuth().getId());
        if (auth.isPresent()) {
          UserAuthentication userAuth = new UserAuthentication(auth.get(), authToken.getJwt());
          SecurityContextHolder.getContext().setAuthentication(userAuth);
          //TODO tokenService.updateExpirationTime(authToken.getId());
        }
      }
    }

    chain.doFilter(request, response);
  }
}
