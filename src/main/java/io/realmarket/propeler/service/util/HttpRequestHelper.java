package io.realmarket.propeler.service.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestHelper {
  private HttpRequestHelper() {}

  public static String getIP(HttpServletRequest request) {
    String ipAddress = request.getHeader("X-FORWARDED-FOR");

    if (StringUtils.isEmpty(ipAddress)) {
      ipAddress = request.getRemoteAddr();
    }

    return ipAddress;
  }
}
