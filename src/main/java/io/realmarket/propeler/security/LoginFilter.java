package io.realmarket.propeler.security;

import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.LoginAttemptsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public final class LoginFilter implements Filter {

  private final LoginAttemptsService loginAttemptsService;

  @Autowired
  public LoginFilter(LoginAttemptsService loginAttemptsService) {
    this.loginAttemptsService = loginAttemptsService;
  }

  @Override
  public void init(FilterConfig filterConfig) {
    log.info("Initiating LoginFilter");
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    log.info(
        " Request - " + httpRequest.getMethod() + " " + httpRequest.getRequestURL().toString());
    try {
      if (loginAttemptsService.isBlocked(AuthenticationUtil.getClientIp())) {
        log.info("Blocked  reguest from  {} ", AuthenticationUtil.getClientIp());
        ((HttpServletResponse) response)
            .sendError(HttpServletResponse.SC_FORBIDDEN, ExceptionMessages.BLOCKED_CLIENT);
        return;
      }
      chain.doFilter(request, response);
    } catch (ServletException | IOException e) {
      log.error("Error in filtering");
    }
  }

  @Override
  public void destroy() {
    log.info("Destroying LoginFilter");
  }
}
