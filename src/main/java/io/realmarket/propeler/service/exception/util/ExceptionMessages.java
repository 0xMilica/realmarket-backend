package io.realmarket.propeler.service.exception.util;

public interface ExceptionMessages {

  String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials provided";

  String USERNAME_ALREADY_EXISTS = "Person with the provided username already exists!";

  String USERNAME_DOES_NOT_EXISTS = "Person with provided username does not exists.";

  String INVALID_REQUEST = "Invalid request!";

  String TOKEN_IS_NOT_PROVIDED = "Token is not provided";

  String EMAIL_SENDING_EXCEPTION = "Email Sending exception: ";

  String INVALID_TOKEN_PROVIDED = "Invalid token provided";

  String INVALID_TOKEN_TYPE = "Invalid token type";

  String COULD_NOT_GENERATE_TOKEN = "Could not generate token";
}
