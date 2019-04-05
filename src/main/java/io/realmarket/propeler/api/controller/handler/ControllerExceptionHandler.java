package io.realmarket.propeler.api.controller.handler;

import io.realmarket.propeler.service.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(value = {Throwable.class})
  protected @ResponseBody ResponseEntity<Object> handleInternalException(
      Exception ex, WebRequest request) {

    final String errorMessage = ex.getMessage();
    HttpStatus status = HttpStatus.BAD_REQUEST;

    if (ex instanceof EntityNotFoundException) {
      status = HttpStatus.NOT_FOUND;
    } else if (ex instanceof InternalServerErrorException || ex instanceof COSException) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    } else if (ex instanceof ForbiddenOperationException) {
      status = HttpStatus.FORBIDDEN;
    } else if (ex instanceof InvalidCaptchaException) {
      status = HttpStatus.FORBIDDEN;
    } else if (ex instanceof ActiveCampaignAlreadyExistsException) {
      status = HttpStatus.CONFLICT;
    }

    log.error(
        "exception: {}, cause: {}, message: {}", ex.toString(), ex.getCause(), ex.getMessage());

    return handleExceptionInternal(
        ex,
        ErrorMessageBuilder.buildMessage(
            errorMessage, status.value(), ex.getClass().getSimpleName()),
        new HttpHeaders(),
        status,
        request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return handleExceptionInternal(
        ex,
        ErrorMessageBuilder.buildMessage(
            ex.getBindingResult(), status.value(), ex.getClass().getSimpleName()),
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST,
        request);
  }
}
