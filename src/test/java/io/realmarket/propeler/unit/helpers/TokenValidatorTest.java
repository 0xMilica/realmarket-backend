package io.realmarket.propeler.unit.helpers;

import io.realmarket.propeler.service.impl.AuthServiceImpl;
import io.realmarket.propeler.service.util.TokenValidator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AuthServiceImpl.class)
public class TokenValidatorTest {

  private static final String TEST_TOKEN = "TEST_TOKEN";
  private static final long TEST_SECONDS_TO_ADJUST = 3600;
  public static final Date TEST_DATE_IN_FUTURE =
      new Date(System.currentTimeMillis() + TEST_SECONDS_TO_ADJUST * 1000);
  public static final Date TEST_DATE_IN_PAST =
      new Date(System.currentTimeMillis() - TEST_SECONDS_TO_ADJUST * 1000);

  @Test
  public void IsValidToken_Should_Return_True() {
    Boolean valid = TokenValidator.isTokenValid(TEST_TOKEN, TEST_DATE_IN_FUTURE);
    Assert.assertTrue(valid == true);
  }

  @Test
  public void IsValidToken__ExpiredDate_Should_Return_False_() {
    Boolean valid = TokenValidator.isTokenValid(TEST_TOKEN, TEST_DATE_IN_PAST);
    Assert.assertTrue(valid == false);
  }

  @Test
  public void IsValidToken_NoExpiredDate_Should_Return_False() {
    Boolean valid = TokenValidator.isTokenValid(TEST_TOKEN, null);
    Assert.assertTrue(valid == false);
  }

  @Test
  public void IsValidToken_NoToken_Should_Return_False() {
    Boolean valid = TokenValidator.isTokenValid(null, TEST_DATE_IN_FUTURE);
    Assert.assertTrue(valid == false);
  }
}
