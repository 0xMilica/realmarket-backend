package io.realmarket.propeler.api.controller.handler;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ErrorMessageBuilder {

  public static Map<String, Object> buildMessage(String message, int status, String error) {
    Map<String, Object> messageData = new HashMap<>();

    messageData.put("timestamp", Instant.now());
    messageData.put("status", status);
    messageData.put("error", error);
    messageData.put("message", message);

    return messageData;
  }

  public static Object buildMessage(BindingResult bindingResult, int status, String error) {
    Map<String, Object> messageData = new HashMap<>();
    ArrayList<String> errors = new ArrayList<>();
    for (ObjectError e : bindingResult.getAllErrors()) {
      errors.add(e.getDefaultMessage());
    }

    messageData.put("timestamp", Instant.now());
    messageData.put("status", status);
    messageData.put("error", error);
    messageData.put("message", errors);

    return messageData;
  }
}
