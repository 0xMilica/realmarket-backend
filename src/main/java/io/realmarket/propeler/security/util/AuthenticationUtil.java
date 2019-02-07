package io.realmarket.propeler.security.util;

import io.realmarket.propeler.security.UserAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtil {
  private AuthenticationUtil() {}

  public static UserAuthentication getAuthentication() {
    return (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
  }
}
