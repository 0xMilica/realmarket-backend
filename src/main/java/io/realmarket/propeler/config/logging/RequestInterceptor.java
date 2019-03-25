package io.realmarket.propeler.config.logging;

import io.realmarket.propeler.security.util.AuthenticationUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

import static io.realmarket.propeler.config.logging.LoggingContext.*;

@Component
@Slf4j
public class RequestInterceptor extends HandlerInterceptorAdapter {
  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    MultiValueMap<String, String> headers = getHeaders(request);
    MDC.put(HTTP_HEADERS, headers.toString());
    MDC.put(
        USER_ID,
        AuthenticationUtil.getAuthentication() == null
            ? ""
            : AuthenticationUtil.getAuthentication().getAuth().getId().toString());
    MDC.put(CORRELATION_ID, request.getHeader("x-request-id"));

    log.trace(
        "HTTP Request - path: {}, method: {}, headers {}, query: {}, body: {}",
        request.getServletPath(),
        request.getMethod(),
        headers,
        request.getQueryString(),
        "TBD");

    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    MDC.clear();
  }

  private MultiValueMap<String, String> getHeaders(HttpServletRequest request) {
    MultiValueMap<String, String> headers = new HttpHeaders();
    for (Enumeration names = request.getHeaderNames(); names.hasMoreElements(); ) {
      String name = (String) names.nextElement();
      for (Enumeration values = request.getHeaders(name); values.hasMoreElements(); ) {
        String value = (String) values.nextElement();
        headers.add(name, value);
      }
    }
    return headers;
  }
}
