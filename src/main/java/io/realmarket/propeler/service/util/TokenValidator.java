package io.realmarket.propeler.service.util;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

public class TokenValidator {

  private TokenValidator(){
    //to hide implicit public constructor
  }

  public static Boolean isTokenValid(String token, Date expirationTime) {
    return Stream.of(token, expirationTime).allMatch(Objects::nonNull)
        && expirationTime.after(new Date());
  }
}
