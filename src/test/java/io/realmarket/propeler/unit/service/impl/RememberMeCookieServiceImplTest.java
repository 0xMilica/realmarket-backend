package io.realmarket.propeler.unit.service.impl;

import io.realmarket.propeler.model.RememberMeCookie;
import io.realmarket.propeler.repository.RememberMeCookieRepository;
import io.realmarket.propeler.service.impl.RememberMeCookieServiceImpl;
import io.realmarket.propeler.service.util.RememberMeCookieHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.Instant;
import java.util.Optional;

import static io.realmarket.propeler.unit.util.AuthUtils.*;
import static io.realmarket.propeler.unit.util.RememberMeCookieUtils.TEST_RM_COOKIE;
import static io.realmarket.propeler.unit.util.RememberMeCookieUtils.TEST_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class RememberMeCookieServiceImplTest {

  @Mock private RememberMeCookieRepository rememberMeCookieRepository;

  @InjectMocks private RememberMeCookieServiceImpl rememberMeCookieService;

  @Test
  public void findByValueAndNotExpired_Should_Return_OptionalOfCookie() {
    when(rememberMeCookieRepository.findByValueAndExpirationTimeGreaterThanEqual(
            anyString(), any(Instant.class)))
        .thenReturn(Optional.of(TEST_RM_COOKIE));

    Optional<RememberMeCookie> retVal =
        rememberMeCookieService.findByValueAndNotExpired(TEST_VALUE);

    assertTrue(retVal.isPresent());
    assertEquals(TEST_VALUE, retVal.get().getValue());
  }

  @Test
  public void createCookie_Should_Return_NewRememberMeCookie() {
    when(rememberMeCookieRepository.save(any(RememberMeCookie.class))).thenReturn(TEST_RM_COOKIE);

    RememberMeCookie retVal = rememberMeCookieService.createCookie(TEST_AUTH);

    assertEquals(TEST_RM_COOKIE, retVal);
  }

  @Test
  public void deleteCookie_Should_DeleteCookie() {
    rememberMeCookieService.deleteCookie(TEST_RM_COOKIE);

    verify(rememberMeCookieRepository, times(1)).delete(TEST_RM_COOKIE);
  }

  @Test
  public void deleteCurrentCookie_Should_DeleteCookie() {
    when(rememberMeCookieRepository.findByValueAndExpirationTimeGreaterThanEqual(
            anyString(), any(Instant.class)))
            .thenReturn(Optional.of(TEST_RM_COOKIE));

    rememberMeCookieService.deleteCurrentCookie(TEST_REQUEST,TEST_RESPONSE);

    verify(rememberMeCookieRepository, times(1)).delete(TEST_RM_COOKIE);
  }
}
