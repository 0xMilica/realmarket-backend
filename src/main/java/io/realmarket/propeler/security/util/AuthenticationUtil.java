package io.realmarket.propeler.security.util;

import io.realmarket.propeler.security.UserAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationUtil {
  private AuthenticationUtil() {}

  public static UserAuthentication getAuthentication() {
    return (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
  }

  public static String getClientIp() {
    final HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    final String xfHeader = request.getHeader("X-Forwarded-For");
    if (xfHeader == null) {
      return request.getRemoteAddr();
    }
    return xfHeader.split(",")[0];
  }

  public static boolean isAuthenticatedUserId(Long id) {
    return id.equals(getAuthentication().getAuth().getId());
  }
}
