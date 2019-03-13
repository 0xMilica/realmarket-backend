package io.realmarket.propeler.api.aspects;

import io.realmarket.propeler.security.util.AuthenticationUtil;
import io.realmarket.propeler.service.exception.InvalidCaptchaException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.CaptchaValidator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class CaptchaAspect {

  private static final String CAPTCHA_HEADER_NAME = "captcha-response";

  private final CaptchaValidator captchaValidator;

  private final HttpServletRequest request;

  @Autowired
  public CaptchaAspect(CaptchaValidator captchaValidator, HttpServletRequest request) {
    this.captchaValidator = captchaValidator;
    this.request = request;
  }

  @Around("@annotation(io.realmarket.propeler.api.annotations.RequireCaptcha)")
  public Object validateCaptcha(ProceedingJoinPoint joinPoint) throws Throwable {

    final String captchaResponse = request.getHeader(CAPTCHA_HEADER_NAME);
    log.info("Recaptcha response provided {} ", captchaResponse);
    if (captchaValidator.isBlocked(AuthenticationUtil.getClientIp())) {
      throw new InvalidCaptchaException(ExceptionMessages.BLOCKED_CLIENT);
    }
    if (!captchaValidator.isCaptchaValid(captchaResponse)) {
      captchaValidator.reCaptchaFailed(AuthenticationUtil.getClientIp());
      throw new InvalidCaptchaException(ExceptionMessages.INVALID_CAPTCHA);
    }
    captchaValidator.reCaptchaSucceeded(AuthenticationUtil.getClientIp());
    return joinPoint.proceed();
  }
}
