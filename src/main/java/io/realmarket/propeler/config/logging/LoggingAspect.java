package io.realmarket.propeler.config.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Arrays;

import static io.realmarket.propeler.config.logging.LoggingContext.CALL_SIGNATURE;
import static io.realmarket.propeler.config.logging.LoggingContext.RETURN_VALUE;

@Configuration
@EnableAspectJAutoProxy
@Aspect
@Slf4j
public class LoggingAspect {

  @Before(
      "execution(* io.realmarket.propeler.api.controller.impl.*.*(..)) "
          + " || execution(* io.realmarket.propeler.service.impl.*.*(..))"
          + " || execution(* io.realmarket.propeler.repository.*.*(..))")
  public void before(JoinPoint joinPoint) {
    MDC.put(
        CALL_SIGNATURE,
        String.format(
            "%s.%s(%s)",
            joinPoint.getSignature().getDeclaringType().getSimpleName(),
            ((MethodSignature) joinPoint.getSignature()).getMethod().getName(),
            Arrays.toString(joinPoint.getArgs())));
    log.trace("Entering method");
    MDC.remove(CALL_SIGNATURE);
  }

  @AfterReturning(
      value =
          "execution(* io.realmarket.propeler.api.controller.impl.*.*(..))"
              + " || execution(* io.realmarket.propeler.service.impl.*.*(..))"
              + " || execution(* io.realmarket.propeler.repository.*.*(..))",
      returning = "returnValue")
  public void afterReturning(JoinPoint joinPoint, Object returnValue) {
    MDC.put(
        CALL_SIGNATURE,
        String.format(
            "%s.%s(%s)",
            joinPoint.getSignature().getDeclaringType().getSimpleName(),
            ((MethodSignature) joinPoint.getSignature()).getMethod().getName(),
            Arrays.toString(joinPoint.getArgs())));

    if (returnValue != null) {
      MDC.put(RETURN_VALUE, returnValue.toString());
    }

    log.trace("Exiting method");

    MDC.remove(CALL_SIGNATURE);
    MDC.remove(RETURN_VALUE);
  }

  @AfterThrowing(
      value =
          "execution(* io.realmarket.propeler.api.controller.impl.*.*(..))"
              + " || execution(* io.realmarket.propeler.service.impl.*.*(..))"
              + " || execution(* io.realmarket.propeler.repository.*.*(..))",
      throwing = "error")
  public void afterThrowing(JoinPoint joinPoint, Throwable error) {
    MDC.put(
        CALL_SIGNATURE,
        String.format(
            "%s.%s(%s)",
            joinPoint.getSignature().getDeclaringType().getSimpleName(),
            ((MethodSignature) joinPoint.getSignature()).getMethod().getName(),
            Arrays.toString(joinPoint.getArgs())));

    log.warn("Exception: {}, message: {}", error.getCause(), error.getMessage());

    MDC.remove(CALL_SIGNATURE);
  }
}
