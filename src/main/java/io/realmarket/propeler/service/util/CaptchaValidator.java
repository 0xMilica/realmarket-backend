package io.realmarket.propeler.service.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.realmarket.propeler.service.util.dto.CaptchaResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
@Slf4j
public class CaptchaValidator {

  private static final String GOOGLE_RECAPTCHA_ENDPOINT =
      "https://www.google.com/recaptcha/api/siteverify";

  private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

  @Value("${google.recaptcha.secret}")
  private String recaptchaSecret;

  @Value("${app.registration-captcha.ban.attempts}")
  private int maxAttempts;

  private LoadingCache<String, Integer> attemptsCache;

  public CaptchaValidator(
      @Value("${app.registration-captcha.ban.time-banned}") final int banExpirationPeriod) {
    super();
    attemptsCache =
        CacheBuilder.newBuilder()
            .expireAfterWrite(banExpirationPeriod, TimeUnit.MINUTES)
            .build(
                new CacheLoader<String, Integer>() {
                  @Override
                  public Integer load(String key) {
                    return 0;
                  }
                });
  }

  public boolean isCaptchaValid(String captchaResponse) {
    final RestTemplate restTemplate = new RestTemplate();

    final MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
    requestMap.add("secret", recaptchaSecret);
    requestMap.add("response", captchaResponse);

    if (!responseSanityCheck(captchaResponse)) {
      log.info("Captcha response is not in the correct format");
      return false;
    }
    final CaptchaResponseDto apiResponse =
        restTemplate.postForObject(GOOGLE_RECAPTCHA_ENDPOINT, requestMap, CaptchaResponseDto.class);

    return apiResponse != null && Boolean.TRUE.equals(apiResponse.getSuccess());
  }

  private boolean responseSanityCheck(final String response) {
    return RESPONSE_PATTERN.matcher(response).matches();
  }

  public void reCaptchaSucceeded(String key) {
    attemptsCache.invalidate(key);
  }

  public void reCaptchaFailed(String key) {
    int attempts = attemptsCache.getUnchecked(key);
    attemptsCache.put(key, ++attempts);
  }

  public boolean isBlocked(String key) {
    return attemptsCache.getUnchecked(key) >= maxAttempts;
  }
}
