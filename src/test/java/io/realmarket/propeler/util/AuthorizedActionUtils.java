package io.realmarket.propeler.util;

import io.realmarket.propeler.model.AuthorizedAction;

import static io.realmarket.propeler.util.TemporaryTokenUtils.TEST_SECRET;

public class AuthorizedActionUtils {
  public static final String TEST_AUTHORIZED_ACTION_DATA = "TEST_AUTHORIZED_ACTION_DATA";

  public static final AuthorizedAction TEST_AUTHORIZED_ACTION =
      AuthorizedAction.builder().id(1L).data(TEST_SECRET).build();
}
